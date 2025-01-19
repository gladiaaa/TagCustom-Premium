package org.TagCustom.tagCustom.utils;

import net.md_5.bungee.api.ChatColor;

public class GradientUtil {

    public static String applyGradient(String text, String gradient) {
        // Le format du gradient attendu : "&#RRGGBB-&#RRGGBB"
        String[] colors = gradient.split("-");
        if (colors.length != 2) {
            return text; // Retourne le texte brut si le gradient est mal formaté
        }

        String startColor = colors[0].replace("&#", ""); // Couleur de départ (sans &#)
        String endColor = colors[1].replace("&#", "");   // Couleur de fin (sans &#)

        // Convertir les couleurs hexadécimales en valeurs RGB
        int startRed = Integer.parseInt(startColor.substring(0, 2), 16);
        int startGreen = Integer.parseInt(startColor.substring(2, 4), 16);
        int startBlue = Integer.parseInt(startColor.substring(4, 6), 16);

        int endRed = Integer.parseInt(endColor.substring(0, 2), 16);
        int endGreen = Integer.parseInt(endColor.substring(2, 4), 16);
        int endBlue = Integer.parseInt(endColor.substring(4, 6), 16);

        // Longueur du texte
        int length = text.length();
        if (length == 0) {
            return text;
        }

        // Construire le texte avec le dégradé
        StringBuilder gradientText = new StringBuilder();
        for (int i = 0; i < length; i++) {
            // Calculer la couleur pour chaque caractère
            double ratio = (double) i / (length - 1);
            int red = (int) (startRed + (endRed - startRed) * ratio);
            int green = (int) (startGreen + (endGreen - startGreen) * ratio);
            int blue = (int) (startBlue + (endBlue - startBlue) * ratio);

            // Ajouter la couleur hexadécimale au texte
            String hexColor = String.format("#%02X%02X%02X", red, green, blue);
            gradientText.append(ChatColor.of(hexColor)).append(text.charAt(i));
        }

        return gradientText.toString();
    }
}
