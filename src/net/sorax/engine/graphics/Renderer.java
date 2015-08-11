package net.sorax.engine.graphics;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
	
	public static void renderBox(float x, float y, float width, float height, float r, float g, float b, float a) {
		glBegin(GL_QUADS);
			glColor4f(r, g, b, a); glVertex2f(x, y);
			glColor4f(r, g, b, a); glVertex2f(x + width, y);
			glColor4f(r, g, b, a); glVertex2f(x + width, y + height);
			glColor4f(r, g, b, a); glVertex2f(x, y + height);
		glEnd();
	}
	
	public static void renderImage(Image image) {
		float x = image.getX();
		float y = image.getY();
		float w = image.getWidth();
		float h = image.getHeight();
		Texture texture = image.getTexture();
		
		float resizeW = texture.getResizeFactorW();
		float resizeH = texture.getResizeFactorH();
		
		texture.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0); glVertex2f(x, y);
			glTexCoord2f(1 * resizeW, 0); glVertex2f(x + w, y);
			glTexCoord2f(1 * resizeW, 1 * resizeH); glVertex2f(x + w, y + h);
			glTexCoord2f(0, 1 * resizeH); glVertex2f(x, y + h);
		glEnd();
		texture.unbind();
	}
	
	public static void renderSprite(Sprite sprite) {
		float x = sprite.getX();
		float y = sprite.getY();
		float w = sprite.getWidth();
		float h = sprite.getHeight();
		float angle = sprite.getAngle();
		int spriteId = sprite.getFrame();
		Texture texture = sprite.getTexture();
		float refactW = texture.getResizeFactorW();
		float refactH = texture.getResizeFactorH();
		
		int nbSpriteW = (int) (texture.getWidth() / sprite.getSpriteWidth());
		int nbSpriteH = (int) (texture.getHeight() / sprite.getSpriteHeight());
		
		if(sprite.getAnimation() != null) {
			spriteId = sprite.getAnimation().getFrame();
		}

		float cellSizeX = 1.0f / nbSpriteW * refactW;
		float cellSizeY = 1.0f / nbSpriteH * refactH;
		
		
		float posX = ((int)(spriteId % nbSpriteW)) * cellSizeX;
		float posY = ((int)(spriteId / nbSpriteH)) * cellSizeY;

		System.out.println("x : " + (posX) + " y : " + (posY));
		
		texture.bind();
		glPushMatrix();
		
		glTranslatef(x + w / 2, y + h / 2, 0);
		glRotatef(angle, 0, 0, 1);
		glTranslatef(-x - w / 2, -y - h / 2, 0);
		
		glBegin(GL_QUADS);
			glTexCoord2f(posX, posY); glVertex2f(x, y);
			glTexCoord2f(posX + cellSizeX, posY); glVertex2f(x + w, y);
			glTexCoord2f(posX + cellSizeX, posY + cellSizeY); glVertex2f(x + w, y + h);
			glTexCoord2f(posX, posY + cellSizeY); glVertex2f(x, y + h);
		glEnd();
		glPopMatrix();
		texture.unbind();
	}
	
	public static void renderString(BitmapFont font, String s, int gridSize, int x, int y, int charWidth, int charHeight) {
		Texture texture = font.getTexture();
		
		texture.bind();
		glBegin(GL_QUADS);
			for(int i = 0; i < s.length(); i++) {
				int asciiCode = s.charAt(i);
				
				final float cellSize = 1.0f / gridSize;
				
				float cellW = ((int)(asciiCode % gridSize)) * cellSize;
				float cellH = ((int)(asciiCode / gridSize)) * cellSize;
				
				glTexCoord2f(cellW, cellH); glVertex2f(x + i * charWidth / 3, y);
				glTexCoord2f(cellW + cellSize, cellH); glVertex2f(x + i * charWidth / 3 + charWidth, y);
				glTexCoord2f(cellW + cellSize, cellH + cellSize); glVertex2f(x + i * charWidth / 3 + charWidth, y + charHeight);
				glTexCoord2f(cellW, cellH + cellSize); glVertex2f(x + i * charWidth / 3, y + charHeight);
			}
		glEnd();
		texture.unbind();
	}
}
