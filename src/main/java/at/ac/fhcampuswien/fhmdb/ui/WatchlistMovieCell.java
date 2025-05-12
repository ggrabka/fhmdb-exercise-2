package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.logic.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class WatchlistMovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label genre = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final JFXButton deleteBtn = new JFXButton("Remove");

    private final VBox layout = new VBox();

    private final ClickEventHandler<Movie> onRemoveClicked;

    public WatchlistMovieCell(ClickEventHandler<Movie> onRemoveClicked) {
        super();
        this.onRemoveClicked = onRemoveClicked;

        // Button-Style
        deleteBtn.setStyle("-fx-background-color: #f5c518;");
        deleteBtn.setOnMouseClicked(e -> {
            if (getItem() != null && onRemoveClicked != null) {
                onRemoveClicked.onClick(getItem());
            }
        });

        // Layout & Style
        title.getStyleClass().add("text-yellow");
        detail.getStyleClass().add("text-white");
        genre.getStyleClass().add("text-white");
        genre.setStyle("-fx-font-style: italic");
        releaseYear.getStyleClass().add("text-white");
        rating.getStyleClass().add("text-white");

        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));
        layout.setPadding(new Insets(10));
        layout.setSpacing(10);
        layout.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setGraphic(null);
            setText(null);
        } else {
            title.setText(movie.getTitle());
            detail.setText(movie.getDescription() != null ? movie.getDescription() : "No description available");
            genre.setText("Genres: " + movie.getGenres().stream().map(Enum::name).collect(Collectors.joining(", ")));
            releaseYear.setText("Release Year: " + movie.getReleaseYear());
            rating.setText("Rating: " + movie.getRating() + "/10");

            detail.setWrapText(true);
            detail.setMaxWidth(this.getScene().getWidth() - 30);

            layout.getChildren().setAll(title, detail, genre, releaseYear, rating, deleteBtn);
            setGraphic(layout);
        }
    }
}

