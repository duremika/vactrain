function incomingMsg(payloadData) {
    const incoming_msg = document.createElement("div");
    incoming_msg.className = "message";
    const received_msg = document.createElement("div");
    received_msg.className = "received_msg";

    const sender = document.createElement("b");
    sender.appendChild(document.createTextNode(payloadData.sender));
    const text = document.createElement("p");
    text.appendChild(document.createTextNode(payloadData.messageText));
    const date = document.createElement("span");
    date.className = "time_date";
    date.appendChild(document.createTextNode(dateConverter(payloadData.date)));


    received_msg.appendChild(sender);
    received_msg.appendChild(text);
    received_msg.appendChild(date);
    incoming_msg.appendChild(received_msg);

    return incoming_msg;
}

function outgoingMsg(payloadData) {
    const outgoing_msg = document.createElement("div");
    outgoing_msg.className = "message";
    const sent_msg = document.createElement("div");
    sent_msg.className = "sent_msg";

    const text = document.createElement("p");
    text.appendChild(document.createTextNode(payloadData.messageText));
    const date = document.createElement("span");
    date.className = "time_date";
    date.appendChild(document.createTextNode(dateConverter(payloadData.date)));

    sent_msg.appendChild(text);
    sent_msg.appendChild(date);
    outgoing_msg.appendChild(sent_msg);

    return outgoing_msg;
}

function dateConverter(inputDate) {
    const date = new Date(inputDate);
    const day = date.getDate();
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const year = date.getFullYear();
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();

    return `${day}.${month}.${year % 100} ${hours}:${minutes}:${seconds}`
}
