import React from 'react'
import { Github, Code, Zap, Bug, BarChart } from 'lucide-react'

const cardStyle: React.CSSProperties = {
  maxWidth: '600px',
  margin: '2rem auto',
  padding: '2rem',
  boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
  borderRadius: '8px',
  fontFamily: 'Arial, sans-serif',
}

const titleStyle: React.CSSProperties = {
  fontSize: '2rem',
  marginBottom: '0.5rem',
  color: '#333',
}

const descriptionStyle: React.CSSProperties = {
  fontSize: '1rem',
  color: '#666',
  marginBottom: '1.5rem',
}

const sectionTitleStyle: React.CSSProperties = {
  fontSize: '1.2rem',
  fontWeight: 'bold',
  marginBottom: '0.5rem',
  color: '#444',
}

const functionListStyle: React.CSSProperties = {
  display: 'flex',
  flexWrap: 'wrap',
  gap: '0.5rem',
  marginBottom: '1.5rem',
}

const functionItemStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  backgroundColor: '#f0f0f0',
  padding: '0.5rem',
  borderRadius: '4px',
  fontSize: '0.9rem',
}

const aboutStyle: React.CSSProperties = {
  fontSize: '1rem',
  lineHeight: '1.5',
  color: '#555',
  marginBottom: '1.5rem',
}

const buttonStyle: React.CSSProperties = {
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: '100%',
  padding: '0.75rem',
  backgroundColor: '#24292e',
  color: 'white',
  border: 'none',
  borderRadius: '4px',
  fontSize: '1rem',
  cursor: 'pointer',
  transition: 'background-color 0.3s',
}

export default function SimpleSoftwareIntro() {
  return (
    <div style={cardStyle}>
      <h1 style={titleStyle}>HappyDeer</h1>
      <p style={descriptionStyle}>Empowering developers with intelligent code analysis</p>

      <h2 style={sectionTitleStyle}>Key Functions:</h2>
      <div style={functionListStyle}>
        <span style={functionItemStyle}><Code size={16} style={{marginRight: '0.5rem'}} /> Static Code Analysis</span>
        <span style={functionItemStyle}><Zap size={16} style={{marginRight: '0.5rem'}} /> Code Optimization</span>
        <span style={functionItemStyle}><Bug size={16} style={{marginRight: '0.5rem'}} /> Bug Detection</span>
        <span style={functionItemStyle}><BarChart size={16} style={{marginRight: '0.5rem'}} /> Performance Profiling</span>
      </div>

      <h2 style={sectionTitleStyle}>About:</h2>
      <p style={aboutStyle}>
      Introducing the Ultimate Self-Pleasure Tracker! This app allows you to quickly log your self-pleasure sessions and 
      effortlessly keep track of your activities. With engaging statistics and visual charts, you can gain better insights 
      into your habits and optimize your self-care routines. Take control of your pleasure experience and enjoy more fulfilling 
      moments, all within our user-friendly interface! And the best part? It's an open-source project, ensuring security and lightweight performance.
      </p>

      <button 
        style={buttonStyle} 
        onClick={() => window.open('https://github.com/codecraft-pro/codecraft', '_blank')}
      >
        <Github size={20} style={{marginRight: '0.5rem'}} />
        github.com/codecraft-pro/codecraft
      </button>
    </div>
  )
}