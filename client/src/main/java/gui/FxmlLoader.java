package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class FxmlLoader {

    private Pane view;

    public Pane getPage(String fileName) throws IOException {
        try
        {
            URL fileUrl = GraphicalUserInterface.class.getResource("../fxml/" + fileName + ".fxml");
            if(fileUrl == null)
            {
                throw new java.io.FileNotFoundException();
            }
            view = new FXMLLoader().load(fileUrl);

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return view;

    }

}
