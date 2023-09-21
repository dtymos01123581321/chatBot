import React, {useEffect, useRef, useState} from 'react';
import axios from "axios";
import './Chatbot.css';

const ChatBot = () => {
    const [messages, setMessages] = useState([]);
    const [response, setResponse] = useState([]);
    const [inputValue, setInputValue] = useState('');
    const [isSelect, setIsSelect] = useState(false);
    const chatRef = useRef(null);

    const scrollToBottom = () => {
        if (chatRef.current) {
            chatRef.current.scrollTop = chatRef.current.scrollHeight;
        }
    };

    const handleInputChange = (e) => {
        setInputValue(e.target.value);
    };

    const handleSelectAnswerChange = (value) => {
        console.log('value  --: ', value);
        setIsSelect(false);
        handleSend(value)
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
    }, [messages, isSelect]);

    useEffect(() => {
        if (response.length && response[0].indexOf('next') !== -1) {
            const newStep = response[0].split('-')[1]
            sendMsg('', newStep);
        }
        if (response.length && response[0].indexOf('yes') !== -1) {
            setIsSelect(true);
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

    const handleSend = (selectValue) => {
        if (inputValue.trim() || selectValue.trim()) {
            const value = inputValue || selectValue;
            setMessages([...messages, { text: value, sender: 'user' }]);
            setInputValue('');

            const item = response.find(f => f.includes(value));
            const itemNoEmpty = response.find(f => f.includes('noEmpty'));
            const action = item || itemNoEmpty;
            if (action) {
                const newStep = action.split('-')[1];
                setTimeout(() => {
                    fetchData(value, newStep).then(data => {
                        setMessages([...messages, { text: value, sender: 'user' }, { text: data.msg, sender: 'bot' }]);
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
                    <div key={index}
                         className={`chat-message ${msg.sender}`}
                         /*style={{ width: `${msg.text.length + 20}%`, maxWidth: '50%'}}*/>
                        <div className="chat-content">{msg.text}</div>
                    </div>
                ))}
                {isSelect && ['yes', 'no'].map((m, index) =>
                    <div key={m + index}
                         className="chat-message select-answer"
                         /*style={{ width: `${m.length + 12}%`, maxWidth: '50%'}}*/
                         onClick={() => handleSelectAnswerChange(m)}>
                        <div className="chat-content">{m}</div>
                </div>)}
            </div>
            <div className="chat-input">
                <input value={inputValue} onChange={handleInputChange} onKeyDown={handleKeyPress}  placeholder="Type your message here..." disabled={isSelect}/>
                <button onClick={handleSend} className="chat-send" disabled={isSelect} />
            </div>
        </div>
    );
}

export default ChatBot;
