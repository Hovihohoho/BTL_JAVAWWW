/* chatbot.js - Phiên bản khớp với Frubana HTML & Có lưu lịch sử */
console.log("Chatbot JS đã chạy!");

const STORAGE_KEY = 'frubana_chat_history';

document.addEventListener("DOMContentLoaded", function() {
    // 1. Khai báo các element dựa trên ID trong file HTML của bạn
    const sendBtn = document.getElementById('sendBtn');      // ID: sendBtn
    const input = document.getElementById('chatInput');      // ID: chatInput
    const toggleBtn = document.getElementById('chatToggle'); // ID: chatToggle
    const closeBtn = document.getElementById('closeChat');   // ID: closeChat

    // 2. Tải lại lịch sử cũ ngay khi vào trang
    loadHistory();

    // 3. Gắn sự kiện (Thay thế cho onclick="" trong HTML)
    if (sendBtn) sendBtn.addEventListener('click', sendMessage);

    if (input) {
        input.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') sendMessage();
        });
    }

    if (toggleBtn) toggleBtn.addEventListener('click', toggleChat);
    if (closeBtn) closeBtn.addEventListener('click', toggleChat);
});

function sendMessage() {
    const input = document.getElementById('chatInput');
    const message = input.value.trim();
    if (!message) return;

    // 1. Hiện tin nhắn user & LƯU
    addMessage(message, 'user', true);
    input.value = '';

    // 2. Hiện typing
    const typing = document.getElementById('typingIndicator');
    if(typing) typing.style.display = 'block';
    scrollToBottom();

    // 3. Gọi API
    fetch('/api/chat/ask', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message: message })
    })
        .then(res => res.json())
        .then(data => {
            if(typing) typing.style.display = 'none';
            addMessage(data.reply, 'bot', true); // Bot trả lời -> LƯU
        })
        .catch(err => {
            console.error(err);
            if(typing) typing.style.display = 'none';
            addMessage("Server đang bận, vui lòng thử lại sau!", 'bot', false); // Lỗi thì không lưu
        });
}

function addMessage(text, sender, save = false) {
    const chatBody = document.getElementById('chatBody');
    const div = document.createElement('div');
    div.classList.add('message', sender);
    div.innerText = text;

    // Chèn trước typing indicator
    const typing = document.getElementById('typingIndicator');
    if (typing && typing.parentNode === chatBody) {
        chatBody.insertBefore(div, typing);
    } else {
        chatBody.appendChild(div);
    }

    scrollToBottom();

    if (save) {
        saveToHistory(text, sender);
    }
}

function saveToHistory(text, sender) {
    let history = JSON.parse(localStorage.getItem(STORAGE_KEY)) || [];
    history.push({ text: text, sender: sender });
    localStorage.setItem(STORAGE_KEY, JSON.stringify(history));
}

function loadHistory() {
    let history = JSON.parse(localStorage.getItem(STORAGE_KEY)) || [];
    // Nếu chưa có lịch sử, có thể hiển thị câu chào mặc định (nếu muốn)
    // Nhưng vì trong HTML bạn đã fix cứng câu "Xin chào", nên ta chỉ cần load tin nhắn cũ thôi.

    history.forEach(msg => {
        addMessage(msg.text, msg.sender, false); // false để không lưu trùng
    });
}

function toggleChat() {
    const chatWindow = document.getElementById('chatWindow');
    if (!chatWindow) return;

    chatWindow.classList.toggle('active');

    // Nếu mở chat -> Focus và cuộn xuống
    if (chatWindow.classList.contains('active')) {
        const noti = document.getElementById('chatNotify');
        if(noti) noti.style.display = 'none';

        setTimeout(() => {
            const input = document.getElementById('chatInput');
            if(input) input.focus();
            scrollToBottom();
        }, 300);
    }
}

function scrollToBottom() {
    const chatBody = document.getElementById('chatBody');
    if(chatBody) chatBody.scrollTop = chatBody.scrollHeight;
}