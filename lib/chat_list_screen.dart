import 'package:flutter/material.dart';
import 'package:linked/chat_list_item.dart';

List<ChatListItem> list = List<ChatListItem>();

class ChatListScreen extends StatefulWidget {
  @override
  _ChatListScreenState createState() => _ChatListScreenState();
}

class _ChatListScreenState extends State<ChatListScreen> {

  @override
  void initState() {
    getData();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(
        title: Text('Chats'),
        centerTitle: true,
        actions: <Widget>[
          IconButton(icon: Icon(Icons.settings, color: Colors.deepPurpleAccent,), onPressed: null),
        ],
      ),
      body: Container(
        child: ListView.builder(
          itemCount: list.length,
          itemBuilder: (BuildContext context, int index){
            return chatListItem(context, list[index]);
        },),
      ),
    );
  }
}

Widget chatListItem(BuildContext context, ChatListItem item){
  return Column(
    children: <Widget>[
       ListTile(
         leading: CircleAvatar(
           radius: 24.0,
           backgroundColor: Colors.black12,
           child: Text(item.userInitial, style: TextStyle(fontSize: 26.0, fontWeight: FontWeight.w500, color: Colors.deepPurple),),
         ),
         title: Text(item.userName, overflow: TextOverflow.ellipsis,),
         subtitle: Text(item.lastMessage, overflow: TextOverflow.ellipsis, maxLines: 1,),
         trailing: Text(item.lastMessageTime),
         onTap: () => openChatScreen(context, item),
      ),

      Divider(height: 1.0,color: Colors.grey,)
    ],
  );
}

void getData(){
  print("getData");
  list.add(new ChatListItem('Aman', "hey there! i'm new here.", TimeOfDay.now()));
  list.add(new ChatListItem('Jagmeet', "hey there!", TimeOfDay.now()));
  list.add(new ChatListItem('Sidhu', "hey Guys!", TimeOfDay.now()));
}

void openChatScreen(BuildContext context, ChatListItem item){

  Navigator.pushNamed(context, '/chatScreen');
}