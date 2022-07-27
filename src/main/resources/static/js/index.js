const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);
let thisUser;
let currentRoom = 'general';
const messages = document.getElementsByClassName('messages').item(0);

stompClient.connect({}, onConnected);

function onConnected(frame) {
    thisUser = frame.headers['user-name'];
    stompClient.subscribe('/chatroom/general', onPublicMessageReceived);
    stompClient.subscribe('/user/' + thisUser + '/private', onPrivateMessageReceived);

    getAllUsers();
    receiveOldMessages();
    stompClient.send("/chat/message",
        {},
        JSON.stringify({sender: thisUser, messageText: 'I`m joined to chat', status: 'JOIN'}))
    const messages = document.getElementsByClassName('messages').item(0);
    setTimeout(() => messages.scrollTo(0, messages.scrollHeight), 50);

}

const onPublicMessageReceived = (payload) => {
    const payloadData = JSON.parse(payload.body);
    switch (payloadData.status) {
        case "JOIN":
            updateUserList({username: payloadData.sender, isOnline: true});
            break;
        case "LEAVE":
            break;
        case "MESSAGE":
            showMessage(payloadData);
            break;
    }
}

const onPrivateMessageReceived = (payload) => {
    const payloadData = JSON.parse(payload.body);
    if (payloadData.status === "MESSAGE") {
        showMessage(payloadData);
    }
}

function sendMessage() {
    const text_form = document.getElementById('text');
    const text = text_form.value;
    if (currentRoom === 'general') {
        stompClient.send("/chat/message", {},
            JSON.stringify({sender: thisUser, messageText: text, status: 'MESSAGE'}));
    } else {
        const payloadData = {sender: thisUser, receiver: currentRoom, messageText: text, status: 'MESSAGE'};
        stompClient.send("/chat/private", {},
            JSON.stringify(payloadData));

        payloadData.date = new Date(); // only for client
        if (currentRoom !== thisUser) {
            showMessage(payloadData);
        }
    }
    setTimeout(() => messages.scrollTo(0, messages.scrollHeight), 50);
    text_form.value = '';
}


function showMessage(payloadData) {
    const messages = document.getElementsByClassName("messages").item(0);
    if (payloadData.sender === thisUser) {
        messages.appendChild(outgoingMsg(payloadData));
    } else {
        messages.appendChild(incomingMsg(payloadData));
    }

}

function selectChat(chatroom) {
    console.log(chatroom);
    if (chatroom === 'general') {
        currentRoom = 'general';
    } else {
        currentRoom = chatroom;
    }

    receiveOldMessages(currentRoom);
}

async function receiveOldMessages() {
    messages.innerHTML = '';
    fetch(`/messages?chat=${currentRoom}`)
        .then(value => value.json())
        .then(msgList => msgList.forEach(
            msg => showMessage(msg)
        ))
}

async function getAllUsers() {
    fetch("/users")
        .then(value => value.json())
        .then(users => users.forEach(
            usr => updateUserList(usr)
        ))
}