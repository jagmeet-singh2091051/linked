
class Message{
  var message;
  String timestamp;
  bool sent;

  Message(var message, String timestamp, {bool sent}){

    this.message = message;
    this.timestamp = timestamp;
    if(sent != null)
      this.sent = sent;
    else
      this.sent = false;
  }
}