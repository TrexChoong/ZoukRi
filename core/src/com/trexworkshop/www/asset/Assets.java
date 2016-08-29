package com.trexworkshop.www.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class Assets {
	public static final String FONTS_DIR = "Fonts/";
	public static final String SOUNDS_DIR = "Sounds/";
	public static final String TEXTURES_DIR = "Textures/";
	private static AssetManager manager;

	/* singleton */
	public static AssetManager getManager() {
		if(manager == null) {
			manager = new AssetManager();
			FileHandleResolver resolver = new InternalFileHandleResolver();
			manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
			manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
			manager.setErrorListener(new AssetErrorListener() {
				public void error (AssetDescriptor asset, Throwable throwable) {
				}
			});
		}
		return manager;
	}
	
	/* FONTS */
	public static String getFontPath(String fileName) {
		return (new StringBuilder()).append(FONTS_DIR).append(fileName).toString();
	}
	/* loads multiple fonts. fonts of different sizes need to be loaded separately */
	public static void loadFonts(int size, String ... fileNames) {
		for (int i = 0; i < fileNames.length; i++) {
			String fontPath = getFontPath(fileNames[i]);
			FreetypeFontLoader.FreeTypeFontLoaderParameter fontParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
			fontParams.fontFileName = fontPath;
			fontParams.fontParameters.size = size;
			getManager().load(size + fontPath, BitmapFont.class, fontParams);
		}
	}

	/* Create Bitmap font with styles(Border,shadow) */
	public static BitmapFont generateFont(int size, int borderWidth, Color borderColor, Color fontColor, String fileNames) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(getFontPath(fileNames)));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = size;
		parameter.borderWidth = borderWidth;
		parameter.borderColor = borderColor;
		parameter.color = fontColor;
		BitmapFont fontLabelStyle = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose();

		return fontLabelStyle;
	}

	/* get Font */
	public static BitmapFont getFont(int size, String fileName) {
		return getManager().get(size + getFontPath(fileName), BitmapFont.class);
	}
	
	/* SOUNDS */
	public static String getSoundPath(String fileName) {
		return (new StringBuilder()).append(SOUNDS_DIR).append(fileName).toString();
	}
	/* loads multiple sounds */
	public static void loadSounds(String ... fileNames) {
		for (int i = 0; i < fileNames.length; i++) {
			getManager().load(getSoundPath(fileNames[i]), Sound.class);
		}
	}
	/* get Sound */
	public static Sound getSound(String fileName) {
		return getManager().get(getSoundPath(fileName), Sound.class);
	}

	/* MUSIC */
	public static String getMusicPath(String fileName) {
		return (new StringBuilder()).append(SOUNDS_DIR).append(fileName).toString();
	}
	/* loads multiple sounds */
	public static void loadMusic(String ... fileNames) {
		for (int i = 0; i < fileNames.length; i++) {
			getManager().load(getMusicPath(fileNames[i]),  Music.class);
		}
	}
	/* get music */
	public static Music getMusic(String fileName) {
		return getManager().get(getMusicPath(fileName), Music.class);
	}

	/* TEXTURES */
	public static String getTexturePath(String fileName) {
		return (new StringBuilder()).append(TEXTURES_DIR).append(fileName).toString();
	}
	/* loads multiple textures */
	public static void loadTextures(String ... fileNames) {
		for (int i = 0; i < fileNames.length; i++) {
			getManager().load(getTexturePath(fileNames[i]), Texture.class);
		}
	}
	/* get Texture */
	public static Texture getTexture(String fileName) {
		Texture texture = getManager().get(getTexturePath(fileName), Texture.class);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return texture;
	}
	/* get Texture and convert to Drawable */
	public static Drawable getDrawable(String fileName) {
		return new TextureRegionDrawable(new TextureRegion(getTexture(fileName)));
	}


	/* dispose if necessary */
	public static void disposeManager() {
		if(manager != null) {
			manager.dispose();
			manager = null;
		}
	}

	/* loads multiple textures */
	public static void unloadAssets(String ... fileNames) {
		for (int i = 0; i < fileNames.length; i++) {
			getManager().unload(getTexturePath(fileNames[i]));
		}
	}

	//New Font System Framework
	public static Pixmap getPixmapFromByteArray (byte[] byteArray) {
		if (byteArray != null && byteArray.length > 0) {
			return new Pixmap(byteArray, 0, byteArray.length);
		}
		return null;
	}
//
//	public static Drawable getTextDrawable (String text, int size, int color, boolean bold)
//	{
//        Pixmap pixmap = getPixmapFromByteArray(KLPoker.getInstance().getDelegate().createFont(text, size, color, bold, -1));
//        Drawable drawable = textureToDrawable(new Texture(pixmap));
//        pixmap.dispose();
//		return drawable;
//	}
//
//	public static Drawable getTextDrawable (String text, int size, int color, boolean bold, String Align)
//	{
//        Pixmap pixmap = getPixmapFromByteArray(KLPoker.getInstance().getDelegate().createFont(text, size, color, bold, -1, Align));
//        Drawable drawable = textureToDrawable(new Texture(pixmap));
//        pixmap.dispose();
//		return drawable;
//	}
//
//	public static Drawable getMultiTextDrawable (String text, int size, int color, boolean bold, float height, float width, boolean ellipses)
//	{
//		Pixmap pixmap = getPixmapFromByteArray(KLPoker.getInstance().getDelegate().drawMultiLineEllipsizedText( text, size, color, bold, height, width, ellipses));
//		Drawable drawable = textureToDrawable(new Texture(pixmap));
//		pixmap.dispose();
//		return drawable;
//	}
//
//	public static Drawable getMultiTextDrawable (String text, int size, int color, boolean bold, float height, float width, boolean ellipses, String AlignS)
//	{
//		Pixmap pixmap = getPixmapFromByteArray(KLPoker.getInstance().getDelegate().drawMultiLineEllipsizedText( text, size, color, bold, height, width, ellipses, AlignS));
//		Drawable drawable = textureToDrawable(new Texture(pixmap));
//		pixmap.dispose();
//		return drawable;
//	}

	public static Drawable textureToDrawable (Texture texture) {
		return new UnmanagedTextureRegionDrawable(new TextureRegion(texture));
	}
	public static class UnmanagedTextureRegionDrawable extends TextureRegionDrawable {
		public UnmanagedTextureRegionDrawable(TextureRegion region) {
			setRegion(region);
		}
	}
}