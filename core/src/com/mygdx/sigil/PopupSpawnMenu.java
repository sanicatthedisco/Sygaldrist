package com.mygdx.sigil;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class PopupSpawnMenu {
    private Table root;
    private Array<Widget> runeIcons;
    private Array<Texture> runeImgs;
    static Array<String> runeImgPaths = new Array<String>();

    static final String PATH_TO_RUNE_IMAGES = "runes/64w/";
    static final String IMAGE_EXTENSION = ".png";
    static float elementSpacing = 30;

    public PopupSpawnMenu() {
        root = new Table();
        runeIcons = new Array<Widget>();
        runeImgs = new Array<Texture>();

        //gives the root table a color
        Pixmap bgPixmap = new Pixmap(1,1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.TAN);
        bgPixmap.fill();
        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
        root.setBackground(textureRegionDrawableBg);
        root.pad(10);

        //adds all the icons for runes
        runeImgPaths.add("ule", "doch", "heat", "kinetic");
        runeImgPaths.add("conversion");
        for (int i = 0; i < runeImgPaths.size; i ++) {
            runeImgs.add(new Texture(PATH_TO_RUNE_IMAGES + runeImgPaths.get(i) + IMAGE_EXTENSION));
        }
        Image temp;
        for (int i = 0; i < runeImgs.size; i ++) {
            temp = new Image(runeImgs.get(i));
            temp.addListener(new DragListener() {
                public void dragStart(InputEvent event, float x, float y, int pointer) {
                    //Rune draggedRune = new Rune(event.getListenerActor())
                }
            });
            runeIcons.add(temp);
            root.add(temp);
            if (i != runeImgs.size-1) {
                root.getCell(temp).padBottom(elementSpacing);
                root.row();
            }
        }
		/*
		stack = new Stack();
		popupMenuTable.add(stack);

		stack.add(uleIcon);
		stack.add(dochIcon);

		 */

    }

    public Table getRoot() {
        return root;
    }
}
