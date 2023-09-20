import React, {useEffect, useRef, useState} from 'react';
import axios from "axios";
import './Chatbot.css';

const ChatBot = () => {
    const [messages, setMessages] = useState([]);
    const [response, setResponse] = useState([]);
    const [inputValue, setInputValue] = useState('');
    const chatRef = useRef(null);

    const scrollToBottom = () => {
        if (chatRef.current) {
            chatRef.current.scrollTop = chatRef.current.scrollHeight;
        }
    };

    const handleInputChange = (e) => {
        setInputValue(e.target.value);
    };

    const fetchData = async (msg, step) => {
        try {
            const response = await axios.post('http://localhost:8080/message', {
                "msg": msg,
                step
            });
            return response.data;
        } catch (error) {
            console.error("Error while receiving data from the server:", error);
        }
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            handleSend()
        }
    }

    useEffect(() => {
        scrollToBottom();
    }, [messages]);

    useEffect(() => {
        if (response.length && response[0].indexOf('next') !== -1) {
            const newStep = response[0].split('-')[1]
            sendMsg('', newStep);
        }
    }, [response]);

    const sendMsg = (msg, step) => {
        fetchData(msg, step).then(data => {
            if (data) {
                setMessages([...messages, {text: data.msg, sender: 'bot'}]);
                setResponse(data.nextSteps);
            }
        });
    }

    useEffect(() => {
        sendMsg('', 0);
    }, []);

    const handleSend = () => {
        if (inputValue.trim()) {
            setMessages([...messages, { text: inputValue, sender: 'user' }]);
            setInputValue('');

            const item = response.find(f => f.includes(inputValue));
            const itemNoEmpty = response.find(f => f.includes('noEmpty'));
            const action = item || itemNoEmpty;
            if (action) {
                const newStep = action.split('-')[1];
                setTimeout(() => {
                    fetchData(inputValue, newStep).then(data => {
                        setMessages([...messages, { text: inputValue, sender: 'user' }, { text: data.msg, sender: 'bot' }]);
                        setResponse(data.nextSteps);
                    })
                }, 1000);
            }
        }
    };

    return (
        <div className="chatbot">
            <div className="chat-history" ref={chatRef}>
                {messages.length && messages.map((msg, index) => (
                    <div key={index} className={`chat-message ${msg.sender}`}>
                        <div className="chat-content">{msg.text}</div>
                    </div>
                ))}
            </div>
            <div className="chat-input">
                <input value={inputValue} onChange={handleInputChange} onKeyDown={handleKeyPress}  placeholder="Type your message here..." />
                <button onClick={handleSend} className="chat-send" />
            </div>
        </div>
    );
}

export default ChatBot;
