import 'package:flutter/material.dart';
import 'package:linked/message.dart';


List<Message> messageList = new List<Message>();

class ChatScreen extends StatefulWidget {
  @override
  _ChatScreenState createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {

  TextEditingController messageController = TextEditingController();

  @override
  void initState() {
    getDataFromDB();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Aman'),
        centerTitle: true,
        leading: CircleAvatar(
          radius: 4.0,
          backgroundColor: Colors.black12,
          child: Text('A', style: TextStyle(fontSize: 26.0, fontWeight: FontWeight.w500, color: Colors.deepPurple),),
        ),
        actions: <Widget>[
          IconButton(icon: Icon(Icons.call, color: Colors.deepPurpleAccent,),),
          IconButton(icon: Icon(Icons.video_call, color: Colors.deepPurpleAccent,),),
        ],
      ),

      body: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        children: <Widget>[
          Expanded(
            child: Container(
              color: Colors.black12,
              child: ListView.builder(
                shrinkWrap: true,
                itemCount: messageList.length,
                itemBuilder: (BuildContext context, int index){
                  return chatItem(messageList[index]);
                },),
            ),
          ),

          Divider(
            color: Colors.black87,
            height: 4.0,
          ),
          Container(
            color: Colors.white70,
            height: 40.0,
            child: Flex(
              direction: Axis.horizontal,
              children: <Widget>[

                IconButton(icon: Icon(Icons.attachment, color: Colors.deepPurpleAccent,), onPressed: null),

                Flexible(child: Container(
                  margin: EdgeInsets.symmetric(vertical: 4.0),
                  alignment: Alignment.centerLeft,
                  padding: EdgeInsets.symmetric(horizontal: 8.0),
                  child: TextField(
                    controller: messageController,
                    style: TextStyle(
                      fontSize: 14.0,
                    ),
                    decoration: InputDecoration(
                      border: InputBorder.none,
                      hintText: 'Type here..',
                    ),
                  ),
                  decoration: BoxDecoration(
                    color: Colors.black12,
                    borderRadius: BorderRadius.all(Radius.circular(32.0)),
                  ),
                )),

                IconButton(icon: Icon(Icons.send, color: Colors.deepPurpleAccent),
                  onPressed: () => setState(() {
                    sendMessage(messageController.text);
                    messageController.clear();
                  }),)
              ],
            ),
          ),
        ],
      ),
    );
  }
}

Widget chatItem(Message message){
  return Container(
    alignment: message.sent ? Alignment.centerRight : Alignment.centerLeft,
    margin: message.sent
        ? EdgeInsets.only(top: 5.0, bottom: 5.0, left: 15.0, right: 5.0)
        : EdgeInsets.only(top: 5.0, bottom: 5.0, left: 5.0, right: 15.0),
    padding: EdgeInsets.all(8.0),
    decoration: BoxDecoration(
      color: Colors.white,
      borderRadius: message.sent
          ? BorderRadius.only(topLeft: Radius.circular(8.0), topRight: Radius.circular(8.0),
          bottomLeft: Radius.circular(8.0), bottomRight: Radius.circular(0.0))
          : BorderRadius.only(topLeft: Radius.circular(8.0), topRight: Radius.circular(8.0),
          bottomLeft: Radius.circular(0.0), bottomRight: Radius.circular(8.0)),
    ),
    child: Flex(
      direction: Axis.horizontal,
      mainAxisAlignment: MainAxisAlignment.start,
      children: <Widget>[
        Expanded(child: Text(message.message)),
        Container(width: 15.0,),
        Text(message.timestamp, style: TextStyle(color: Colors.black54, fontSize: 10.0),),
      ],
    ),
  );
}

void getDataFromDB(){
  messageList.add(new Message("Hello", "11:35"));
  messageList.add(new Message("Hi There!", "11:36", sent: true));
  messageList.add(new Message("How are you?", "11:36"));
  messageList.add(new Message("I'm good, what about you?", "11:37", sent: true));
}

void sendMessage(var msg){
  var currentTime = DateTime.now();
  String time = currentTime.hour.toString() + ":" + currentTime.minute.toString();
  messageList.add(new Message(msg, time, sent: true));
}