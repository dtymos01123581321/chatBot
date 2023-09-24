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

    const handleSelectAnswerChange = (value, step) => {
        console.log('value, step  --: ', value, step);
        handleSend(value, step)
        setResponse([])
    };

    const fetchData = async (msg, step) => {
        try {
            const response = await axios.post('http://localhost:8080/api/chatbot/message', {
                "msg": msg,
                step
            });
            return response.data;
        } catch (error) {
            console.error("Error while receiving data from the server:", error);
        }
    };

    const fetchGptData = async (msg, step) => {
        try {
            const response = await axios.post('http://localhost:8080/api/chatbot/gpt', {
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
            handleSend(inputValue, response[0].step)
        }
    }

    useEffect(() => {
        scrollToBottom();
    }, [messages, response]);

    useEffect(() => {
        if (response.length === 1) {
            const { type, step } = response[0];
            if (type === 'next') {
                sendMsg('', step);
            }
        } else {

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

    const handleSend = (value, newStep) => {
        if (inputValue.trim() || value.trim()) {
            setMessages([...messages, { text: value, sender: 'user' }]);
            setInputValue('');
            setTimeout(() => {
                if(response[0].type === 'openai') {
                    fetchGptData(value, newStep).then(data => {
                        setMessages([...messages, { text: value, sender: 'user' }, { text: data.msg, sender: 'bot' }]);
                        setResponse(data.nextSteps);
                    })
                } else {
                    fetchData(value, newStep).then(data => {
                        setMessages([...messages, { text: value, sender: 'user' }, { text: data.msg, sender: 'bot' }]);
                        setResponse(data.nextSteps);
                    })
                }
            }, 1000);
        }
    };

    const onSubmit = () => {
        handleSend(inputValue, response[0].step)
    }

    return (
        <div className="chatbot">
            <div className="chat-history" ref={chatRef}>
                {messages.length && messages.map((msg, index) => (
                    <div key={index} className={`chat-message ${msg.sender}`}>
                        <div className="chat-content">{msg.text}</div>
                    </div>
                ))}
                {response.length > 1 && response.map((m, index) =>
                    <div key={m.type + index}
                         className="chat-message select-answer"
                         onClick={() => handleSelectAnswerChange(m.type, m.step)}>
                        <div className="chat-content">{m.type}</div>
                </div>)}
            </div>
            {response.length === 1 && <div className="chat-input">
                <input value={inputValue}
                       onChange={handleInputChange}
                       onKeyDown={handleKeyPress}
                       placeholder="Type your message here..."
                       disabled={response.length > 1}/>
                <button
                    onClick={onSubmit}
                    className="chat-send"
                />
            </div>}
        </div>
    );
}

export default ChatBot;
