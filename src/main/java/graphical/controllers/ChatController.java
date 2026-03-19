package graphical.controllers;
import entities.dtos.FriendshipDTO;
import entities.dtos.MessageDTO;
import entities.friendships.FriendshipStatus;
import entities.friendships.UserFriendship;
import entities.messages.Message;
import entities.messages.ReplyMessage;
import entities.users.AbstractUser;
import exceptions.ValidationException;
import graphical.AppServiceManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import services.DuckService;
import services.FriendshipService;
import services.MessageService;
import services.PersonService;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChatController {

    @SuppressWarnings("unused")
    @FXML private ListView<AbstractUser> contactList;
    @SuppressWarnings("unused")
    @FXML private Label currentContactLabel;
    @SuppressWarnings("unused")
    @FXML private ScrollPane messageScrollPane;
    @SuppressWarnings("unused")
    @FXML private VBox messageContainer;
    @SuppressWarnings("unused")
    @FXML private TextField messageInput;
    @SuppressWarnings("unused")
    @FXML private Label currentUserLabel;
    @SuppressWarnings("unused")
    @FXML private Circle profileCircle;
    @SuppressWarnings("unused")
    @FXML private TextField friendRequestInput;
    @SuppressWarnings("unused")
    @FXML private Hyperlink logoutLink;
    @SuppressWarnings("unused")
    @FXML private HBox replyPreviewContainer;
    @SuppressWarnings("unused")
    @FXML private Label replyAuthorLabel;
    @SuppressWarnings("unused")
    @FXML private Label replyContentLabel;
    @SuppressWarnings("unused")
    @FXML private StackPane notificationIconContainer;
    @SuppressWarnings("unused")
    @FXML private Circle notificationDot;
    @SuppressWarnings("unused")
    @FXML private Label notificationCountLabel;

    private AbstractUser currentUser;
    private AbstractUser currentFriend;
    private Stage currentStage;
    private MessageService messageService;
    private FriendshipService friendshipService;
    private DuckService duckService;
    private PersonService personService;
    private AppServiceManager appServiceManager;
    private Message messageToReply = null;
    private LocalDateTime oldestMessageTimestamp = null;
    private boolean isLoading = false;
    private boolean hasMoreMessages = true;
    private boolean forceScrollToBottom = false;

    private final Popup notificationPopup = new Popup();

    @FXML
    @SuppressWarnings("unused")
    public void initialize() {

        contactList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(AbstractUser item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getUsername());
                }
            }
        });

        contactList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(currentFriend)) {
                onFriendSelected(newVal);
            }
        });

        messageScrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 0.0 && !isLoading && hasMoreMessages && currentFriend != null) {
                loadMessages(false);
            }
        });

        messageContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (forceScrollToBottom) {
                messageContainer.applyCss();
                messageContainer.layout();
                messageScrollPane.setVvalue(1.0);
                forceScrollToBottom = false;
            }
        });

        notificationPopup.setAutoHide(true);
    }

    @FXML
    @SuppressWarnings("unused")
    private void onShowNotifications() {
        if (currentUser == null) return;
        if (notificationPopup.isShowing()) {
            notificationPopup.hide();
            return;
        }
        refreshNotificationPopup();
    }

    private void refreshNotificationPopup() {
        Set<UserFriendship> allNonApproved = friendshipService.getNonApprovedFriendships(currentUser.getId());

        List<UserFriendship> pendingRequests = allNonApproved.stream()
                .filter(f -> f.getStatus() == FriendshipStatus.PENDING && f.getReceiver().getId() == currentUser.getId())
                .toList();

        List<UserFriendship> rejectedRequests = allNonApproved.stream()
                .filter(f -> f.getStatus() == FriendshipStatus.REJECTED && f.getReceiver().getId() == currentUser.getId())
                .toList();

        VBox rootContainer = new VBox(15);
        rootContainer.getStyleClass().add("popup-card");
        rootContainer.setPrefWidth(350);

        Label title = new Label("Friend Requests");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1565C0;");
        rootContainer.getChildren().add(title);

        VBox listContent = new VBox(10);

        if (pendingRequests.isEmpty() && rejectedRequests.isEmpty()) {
            Label noReq = new Label("No new requests");
            noReq.setStyle("-fx-text-fill: #555; -fx-font-style: italic;");
            listContent.getChildren().add(noReq);
        } else {
            for (UserFriendship request : pendingRequests) {
                listContent.getChildren().add(createRequestRow(request));
            }
            if (!rejectedRequests.isEmpty()) {
                Label rejectedHeader = new Label("Rejected History");
                rejectedHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #C62828; -fx-padding: 10 0 5 0;");
                listContent.getChildren().add(new Separator());
                listContent.getChildren().add(rejectedHeader);
                for (UserFriendship request : rejectedRequests) {
                    listContent.getChildren().add(createRequestRow(request));
                }
            }
        }

        ScrollPane scroller = new ScrollPane(listContent);
        scroller.setMaxHeight(300);
        scroller.setFitToWidth(true);
        scroller.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        rootContainer.getChildren().add(scroller);

        notificationPopup.getContent().clear();
        notificationPopup.getContent().add(rootContainer);

        if (!notificationPopup.isShowing()) {
            if (currentStage != null) {
                double x = currentStage.getX() + (currentStage.getWidth() / 2) - 175;
                double y = currentStage.getY() + (currentStage.getHeight() / 2) - 200;
                notificationPopup.show(currentStage, x, y);
            } else {
                notificationPopup.show(notificationIconContainer.getScene().getWindow());
            }
        }
    }

    private HBox createRequestRow(UserFriendship friendship) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #E3F2FD; -fx-background-radius: 10; -fx-padding: 8;");

        Label nameLabel = new Label(friendship.getSender().getUsername());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        row.getChildren().add(nameLabel);

        if (friendship.getStatus() == FriendshipStatus.PENDING) {
            Button acceptBtn = new Button("✔");
            acceptBtn.getStyleClass().add("action-btn-accept");
            acceptBtn.setOnAction(e -> {
                friendship.setStatus(FriendshipStatus.APPROVED);
                friendshipService.updateFriendshipStatus(friendship);
            });

            Button rejectBtn = new Button("X");
            rejectBtn.getStyleClass().add("action-btn-reject");
            rejectBtn.setOnAction(e -> {
                friendship.setStatus(FriendshipStatus.REJECTED);
                friendshipService.updateFriendshipStatus(friendship);
            });

            row.getChildren().addAll(acceptBtn, rejectBtn);
        }

        return row;
    }

    private void updateNotificationBadge() {
        if (currentUser == null || friendshipService == null) return;

        Set<UserFriendship> nonApproved = friendshipService.getNonApprovedFriendships(currentUser.getId());
        long pendingCount = nonApproved.stream()
                .filter(f -> f.getStatus() == FriendshipStatus.PENDING && f.getReceiver().getId() == currentUser.getId())
                .count();

        if (pendingCount > 0) {
            notificationDot.setVisible(true);
            notificationCountLabel.setText(String.valueOf(pendingCount));
            notificationCountLabel.setVisible(true);
        } else {
            notificationDot.setVisible(false);
            notificationCountLabel.setVisible(false);
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void onSendMessage() {
        String text = messageInput.getText();
        if (text == null || text.trim().isEmpty() || currentFriend == null) return;

        try {
            MessageDTO dto = new MessageDTO();
            dto.setSender(currentUser);
            dto.setContent(text);
            Set<AbstractUser> recipients = new HashSet<>();
            recipients.add(currentFriend);
            dto.setRecipient(recipients);
            dto.setQuotedMessage(messageToReply);

            messageService.AddMessage(dto);
            messageInput.clear();
            cancelReply();

        } catch (ValidationException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void onFriendSelected(AbstractUser friend) {
        this.currentFriend = friend;
        currentContactLabel.setText(friend.getUsername());
        messageContainer.getChildren().clear();
        hasMoreMessages = true;
        oldestMessageTimestamp = null;
        loadMessages(true);
        cancelReply();
    }

    private void loadMessages(boolean isInitialLoad) {
        if (isLoading) return;
        isLoading = true;

        try {
            int PAGE_SIZE = 10;
            List<Message> messages = messageService.getUserConversation(
                    currentUser.getId(),
                    currentFriend.getId(),
                    oldestMessageTimestamp,
                    PAGE_SIZE
            );

            if (messages.isEmpty()) {
                hasMoreMessages = false;
                isLoading = false;
                return;
            }

            oldestMessageTimestamp = messages.getLast().getTimestamp();

            if (isInitialLoad) {
                Collections.reverse(messages);
                messageContainer.getChildren().clear();
                for (Message msg : messages) {
                    addMessageBubbleToUI(msg, true);
                }
                forceScrollToBottom = true;
            } else {
                double heightBefore = messageContainer.getBoundsInLocal().getHeight();

                for (Message msg : messages) {
                    addMessageBubbleToUI(msg, false);
                }

                messageContainer.applyCss();
                messageContainer.layout();
                messageScrollPane.layout();

                double heightAfter = messageContainer.getBoundsInLocal().getHeight();
                double addedHeight = heightAfter - heightBefore;
                double viewportHeight = messageScrollPane.getViewportBounds().getHeight();
                double scrollableRange = heightAfter - viewportHeight;

                if (scrollableRange > 0) {
                    double newVvalue = addedHeight / scrollableRange;
                    messageScrollPane.setVvalue(newVvalue);
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        } finally {
            isLoading = false;
        }
    }

    private void activateReplyMode(Message message) {
        this.messageToReply = message;
        replyPreviewContainer.setManaged(true);
        replyPreviewContainer.setVisible(true);
        replyAuthorLabel.setText("Reply to " + message.getSender().getUsername());
        String preview = message.getContent().replace("\n", " ");
        if (preview.length() > 50) preview = preview.substring(0, 50) + "...";
        replyContentLabel.setText(preview);
        messageInput.requestFocus();
    }

    @FXML
    private void cancelReply() {
        this.messageToReply = null;
        replyPreviewContainer.setManaged(false);
        replyPreviewContainer.setVisible(false);
    }

    private void addMessageBubbleToUI(Message message, boolean appendToBottom) {
        boolean sentByMe = Objects.equals(message.getSender().getUsername(), currentUser.getUsername());
        VBox bubble = new VBox();
        bubble.getStyleClass().addAll("message-bubble", sentByMe ? "message-sent" : "message-received");
        bubble.setMaxWidth(400);

        if (message instanceof ReplyMessage replyMsg) {
            Message original = replyMsg.getQuotedMessage();
            if (original != null) {
                VBox quotedBox = new VBox();
                quotedBox.getStyleClass().addAll("quoted-message-box", sentByMe ? "quoted-box-sent" : "quoted-box-received");
                Label qAuthor = new Label(original.getSender().getUsername());
                qAuthor.getStyleClass().add("quoted-author");
                String qTextContent = original.getContent().replace("\n", " ");
                if (qTextContent.length() > 50) qTextContent = qTextContent.substring(0, 50) + "...";
                Label qText = new Label(qTextContent);
                qText.getStyleClass().add("quoted-text");
                quotedBox.getChildren().addAll(qAuthor, qText);
                bubble.getChildren().add(quotedBox);
            }
        }

        Label messageLabel = new Label(message.getContent());
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px;");
        Label timeLabel = new Label(message.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.getStyleClass().add("timestamp");
        timeLabel.setMaxWidth(Double.MAX_VALUE);
        timeLabel.setAlignment(Pos.BOTTOM_RIGHT);
        bubble.getChildren().addAll(messageLabel, timeLabel);

        HBox alignmentBox = new HBox();
        HBox.setHgrow(alignmentBox, Priority.ALWAYS);
        alignmentBox.setFillHeight(false);
        alignmentBox.prefWidthProperty().bind(messageContainer.widthProperty().subtract(20));
        alignmentBox.setAlignment(sentByMe ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        alignmentBox.getChildren().add(bubble);

        bubble.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem replyItem = new MenuItem("Reply");
                replyItem.setOnAction(e -> activateReplyMode(message));
                contextMenu.getItems().add(replyItem);
                contextMenu.show(bubble, event.getScreenX(), event.getScreenY());
            }
        });

        if (appendToBottom) {
            messageContainer.getChildren().add(alignmentBox);
        } else {
            messageContainer.getChildren().addFirst(alignmentBox);
        }
    }

    public void setCurrentUser(AbstractUser currentUser) {
        this.currentUser = currentUser;
        if (currentUser != null) {
            currentUserLabel.setText(currentUser.getUsername());
            loadFriends();
            updateNotificationBadge();
        }
    }

    private void loadFriends() {
        if (currentUser == null) return;

        if (currentUser.getFriends() != null) {
            contactList.getItems().setAll(currentUser.getFriends());
        }
    }

    public void setAppServiceManager(AppServiceManager appServiceManager, Stage stage) {
        currentStage = stage;
        this.appServiceManager = appServiceManager;
        messageService = appServiceManager.getMessageService();
        friendshipService = appServiceManager.getFriendshipService();
        duckService = appServiceManager.getDuckService();
        personService = appServiceManager.getPersonService();

        if (this.messageService != null) {
            this.messageService.getMessageChangeProperty().addListener(
                    (observable, oldMessage, newMessage) -> {
                        if (newMessage != null) handleIncomingNotification(newMessage);
                    }
            );
        }

        if (this.friendshipService != null) {
            this.friendshipService.getFriendshipChangeProperty().addListener((observable, oldVal, newVal) -> {
                if (newVal != null && currentUser != null) {

                    boolean isSender = newVal.getSender().getId() == currentUser.getId();
                    boolean isReceiver = newVal.getReceiver().getId() == currentUser.getId();

                    if (isSender || isReceiver) {
                        updateNotificationBadge();

                        if (newVal.getStatus() == FriendshipStatus.APPROVED) {
                            AbstractUser newFriend = isSender ? newVal.getReceiver() : newVal.getSender();

                            boolean alreadyExists = false;
                            if (currentUser.getFriends() != null) {
                                alreadyExists = currentUser.getFriends().stream()
                                        .anyMatch(u -> u.getId() == newFriend.getId());
                            }

                            if (!alreadyExists) {
                                currentUser.addFriend(newFriend);
                            }

                            loadFriends();
                        }

                        if (notificationPopup.isShowing()) {
                            refreshNotificationPopup();
                        }
                    }
                }
            });
        }
    }

    private void handleIncomingNotification(Message message) {
        if (currentUser == null || currentFriend == null) return;
        String sender = message.getSender().getUsername();
        if (sender.equals(currentUser.getUsername())) {
            addMessageBubbleToUI(message, true);
            forceScrollToBottom = true;
            return;
        }
        if (sender.equals(currentFriend.getUsername())) {
            addMessageBubbleToUI(message, true);
            forceScrollToBottom = true;
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void onSendFriendRequest() {
        String username = friendRequestInput.getText();
        if (username == null || username.trim().isEmpty()) return;
        if (currentUser.getUsername().equals(username)) {
            showAlert(Alert.AlertType.WARNING, "Warning", "You cannot send a request to yourself.");
            return;
        }

        try {

            AbstractUser targetUser = personService.findPersonByUsername(username);
            if (targetUser == null) {
               targetUser = duckService.findDuckByUsername(username);
               if (targetUser == null) {
                   showAlert(Alert.AlertType.WARNING, "Warning", "No user found with that username.");
               }
            }

            FriendshipDTO dto = new FriendshipDTO(currentUser, targetUser);

            friendshipService.addFriendship(dto);

            friendRequestInput.clear();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Friend request sent to " + username);

        } catch (ValidationException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    @SuppressWarnings("unused")
    private void onLogout() throws IOException {
        currentUser = null;
        currentFriend = null;
        messageContainer.getChildren().clear();
        contactList.getItems().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/loginView.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        Stage loginStage = new Stage();
        loginController.setAppServiceManager(this.appServiceManager, loginStage);
        Scene scene = new Scene(root);
        URL css = getClass().getResource("/gui/css/loginStyle.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
        loginStage.setScene(scene);
        loginStage.setTitle("Login");
        loginStage.show();
        if (currentStage != null) currentStage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}