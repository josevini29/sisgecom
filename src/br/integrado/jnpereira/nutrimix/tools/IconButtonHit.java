package br.integrado.jnpereira.nutrimix.tools;

import javafx.geometry.NodeOrientation;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconButtonHit {

    private final static String dsCaminhoIcon = "/br/integrado/jnpereira/nutrimix/icon/";
    public final static Object[] ICON_PESQUISA = new Object[]{"lista.png", 14.0, 12.0};
    public final static Object[] ICON_ADD = new Object[]{"add.png", 23.0, 21.0};
    public final static Object[] ICON_EXCLUIR = new Object[]{"excluir.png", 23.0, 21.0};
    public final static Object[] ICON_REFRESH = new Object[]{"refresh.png", 23.0, 21.0};
    public final static Object[] ICON_MESA = new Object[]{"mesa.png", 43.0, 96.0};
    public final static Object[] ICON_DINHEIRO = new Object[]{"dinheiro.png", 17.0, 23.0};

    public static void setIcon(Button button, Object[] imagem) {
        String dsCaminhoImage = (String) imagem[0];
        double vlHeight = (double) imagem[1];
        double vlWidth = (double) imagem[2];
        ImageView image = new ImageView();
        image.setFitHeight(vlHeight);
        image.setFitWidth(vlWidth);
        image.setPickOnBounds(true);
        image.setPreserveRatio(true);
        image.setImage(new Image(dsCaminhoIcon + dsCaminhoImage));
        button.setGraphic(image);
        button.setMinSize(30, 30);
        button.setMaxSize(30, 30);
        button.setPrefSize(30, 30);
    }

    public static void setIconComTexto(Button button, Object[] imagem) {
        String dsCaminhoImage = (String) imagem[0];
        double vlHeight = (double) imagem[1];
        double vlWidth = (double) imagem[2];
        ImageView image = new ImageView();
        image.setFitHeight(vlHeight);
        image.setFitWidth(vlWidth);
        image.setPickOnBounds(true);
        image.setPreserveRatio(true);
        image.setImage(new Image(dsCaminhoIcon + dsCaminhoImage));
        button.setGraphic(image);
    }

    public static void setIconAtend(Button button, Object[] imagem) {
        String dsCaminhoImage = (String) imagem[0];
        double vlHeight = (double) imagem[1];
        double vlWidth = (double) imagem[2];
        ImageView image = new ImageView();
        image.setFitHeight(vlHeight);
        image.setPreserveRatio(true);
        image.setPickOnBounds(true);
        image.setNodeOrientation(NodeOrientation.INHERIT);
        image.setImage(new Image(dsCaminhoIcon + dsCaminhoImage));
        button.setGraphic(image);
    }

}
