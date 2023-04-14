package kaba4cow.ascii.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.TextureLoader;

import kaba4cow.ascii.toolbox.maths.Maths;

public class Renderer {

	private static final String VERTEX_SOURCE = "#version 110\n" + //
			"attribute vec2 position;\n" + //
			"out vec2 uv;\n" + //
			"uniform vec2 window_scale;\n" + //
			"uniform vec2 translation;\n" + //
			"uniform vec2 glyph;\n" + //
			"void main(void) {\n" + //
			"vec2 pos = 32.0 * (position + translation) / window_scale;\n" + //
			"gl_Position = vec4(pos.x - 1.0, -pos.y + 1.0, 0.0, 1.0);\n" + //
			"uv = (position + glyph) / 32.0;\n" + //
			"}";

	private static final String FRAGMENT_SOURCE = "#version 110\n" + //
			"in vec2 uv;\n" + //
			"out vec4 out_color;\n" + //
			"uniform vec3 b_color;\n" + //
			"uniform vec3 f_color;\n" + //
			"uniform sampler2D atlas;\n" + //
			"void main(void) {\n" + //
			"vec4 atlas_color = texture2D(atlas, uv);\n" + //
			"if (atlas_color.r > 0.0) out_color = vec4(f_color, 1.0);\n" + //
			"else out_color = vec4(b_color, 1.0);\n" + //
			"}";

	private static final float[] VERTICES = { 0f, 1f, 0f, 0f, 1f, 0f, 1f, 1f };
	private static final int[] INDICES = { 0, 1, 3, 3, 1, 2 };

	private static int shader_id;

	private static int window_scale_location;
	private static int translation_location;
	private static int glyph_location;
	private static int b_color_location;
	private static int f_color_location;

	private static int texture_id;
	private static int vao_id;

	private static int vertices_vbo_id;
	private static int indices_vbo_id;

	private Renderer() {

	}

	public static void init() {
		int vertex_shader_id = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(vertex_shader_id, VERTEX_SOURCE);
		GL20.glCompileShader(vertex_shader_id);

		int fragment_shader_id = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(fragment_shader_id, FRAGMENT_SOURCE);
		GL20.glCompileShader(fragment_shader_id);

		shader_id = GL20.glCreateProgram();
		GL20.glAttachShader(shader_id, vertex_shader_id);
		GL20.glAttachShader(shader_id, fragment_shader_id);

		GL20.glBindAttribLocation(shader_id, 0, "position");
		GL20.glLinkProgram(shader_id);

		window_scale_location = GL20.glGetUniformLocation(shader_id, "window_scale");
		translation_location = GL20.glGetUniformLocation(shader_id, "translation");
		glyph_location = GL20.glGetUniformLocation(shader_id, "glyph");
		b_color_location = GL20.glGetUniformLocation(shader_id, "b_color");
		f_color_location = GL20.glGetUniformLocation(shader_id, "f_color");

		GL20.glValidateProgram(shader_id);
		GL20.glDetachShader(shader_id, vertex_shader_id);
		GL20.glDetachShader(shader_id, fragment_shader_id);
		GL20.glDeleteShader(vertex_shader_id);
		GL20.glDeleteShader(fragment_shader_id);
		GL20.glUseProgram(shader_id);

		int texture = 0;
		try {
			InputStream input = Renderer.class.getClassLoader().getResourceAsStream("kaba4cow/ascii/core/CP.bmp");
			texture = TextureLoader.getTexture("BMP", input).getTextureID();
		} catch (IOException e) {
			e.printStackTrace();
		}
		texture_id = texture;
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		vao_id = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao_id);

		vertices_vbo_id = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertices_vbo_id);
		FloatBuffer vertices_buffer = BufferUtils.createFloatBuffer(VERTICES.length);
		vertices_buffer.put(VERTICES);
		vertices_buffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices_buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);

		indices_vbo_id = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indices_vbo_id);
		IntBuffer indices_buffer = BufferUtils.createIntBuffer(INDICES.length);
		indices_buffer.put(INDICES);
		indices_buffer.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices_buffer, GL15.GL_STATIC_DRAW);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_ACTIVE_TEXTURE, texture_id);
	}

	public static void update() {
		GL20.glUniform2f(window_scale_location, Display.getWidth(), Display.getHeight());
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0f, 0f, 0f, 1f);
	}

	public static void draw(int x, int y, int glyph, int br, int bg, int bb, int fr, int fg, int fb) {
		GL20.glUniform2f(translation_location, x, y);
		GL20.glUniform2f(glyph_location, glyph % 32, glyph / 32);

		GL20.glUniform3f(b_color_location, br * Maths.DIV255, bg * Maths.DIV255, bb * Maths.DIV255);
		GL20.glUniform3f(f_color_location, fr * Maths.DIV255, fg * Maths.DIV255, fb * Maths.DIV255);
		draw();
	}

	public static void draw() {
		GL11.glDrawElements(GL11.GL_TRIANGLES, INDICES.length, GL11.GL_UNSIGNED_INT, 0);
	}

	public static void destroy() {
		GL20.glUseProgram(0);
		GL20.glDeleteProgram(shader_id);
		GL11.glDeleteTextures(texture_id);
		GL30.glDeleteVertexArrays(vao_id);
		GL15.glDeleteBuffers(vertices_vbo_id);
		GL15.glDeleteBuffers(indices_vbo_id);
	}

}
