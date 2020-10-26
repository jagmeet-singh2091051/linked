

class ChatListItem {
  String userName;
  String userInitial;
  var userImage;
  var lastMessage;
  var lastMessageTime;

  bool hasImage;

  ChatListItem(String userName, var lastMessage, var lastMessageTime){
    this.userName = userName;
    this.lastMessage = lastMessage;
    this.lastMessageTime = lastMessageTime;

    this.userInitial = userName.substring(0, 1).toUpperCase();
    this.lastMessageTime = "00:00";
  }

}