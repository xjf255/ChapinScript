// Frontend/src/App.jsx
import { useEffect } from 'react'
import './App.css'

// Unos íconos SVG sencillos para acompañar los títulos
const IconDoc = () => <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5"><path d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5S19.832 5.477 21 6.253v13C19.832 18.477 18.246 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" /></svg>
const IconCode = () => <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5"><path d="M17.25 6.75 22.5 12l-5.25 5.25m-10.5 0L1.5 12l5.25-5.25m3.75-3 3 16.5" /></svg>
const IconCheck = () => <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5"><path d="m4.5 12.75 6 6 9-13.5" /></svg>
const IconBook = () => <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5"><path d="m3.75 13.5 10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75Z" /></svg>
const IconHome = () => <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5"><path d="m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25" /></svg>
const IconQuestion = () => <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5"><path d="M9.879 7.519c1.171-1.025 3.071-1.025 4.242 0 1.172 1.025 1.172 2.687 0 3.712-.203.179-.43.326-.67.442-.745.361-1.45.999-1.45 1.827v.75M21 12a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9 5.25h.008v.008H12v-.008Z" /></svg>
const IconAlert = () => <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5"><path d="M12 9v3.75m9-.75a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9 3.75h.008v.008H12v-.008Z" /></svg>

function App() {

  // Pequeña lógica para calcular la posición del mouse en cada tarjeta
  useEffect(() => {
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
      card.onmousemove = (e) => {
        const rect = card.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;
        card.style.setProperty('--mouse-x', `${x}px`);
        card.style.setProperty('--mouse-y', `${y}px`);
      };
    });
  }, []);

  const docPoints = [
    { title: "Documentar la instalación y configuración", icon: <IconDoc />, text: "Guía paso a paso para configurar el entorno de desarrollo y producción." },
    { title: "Describir la sintaxis y características del lenguaje soportado", icon: <IconCode />, text: "Manual detallado de las palabras clave, estructuras y tipos del lenguaje." },
    { title: "Incluir ejemplos de uso y casos prácticos", icon: <IconCheck />, text: "Fragmentos de código reales y tutoriales para resolver problemas comunes." },
    { title: "Redactar la introducción y visión general del transpilador", icon: <IconBook />, text: "Explicación de alto nivel de qué es el transpilador, por qué existe y cómo funciona." },
    { title: "Estructurar la página principal de la documentación", icon: <IconHome />, text: "Diseño del índice y la navegación para facilitar la búsqueda de información." },
    { title: "Agregar sección de preguntas frecuentes (FAQ)", icon: <IconQuestion />, text: "Respuestas a las dudas más comunes de los usuarios y desarrolladores." },
    { title: "Documentar errores comunes y su solución", icon: <IconAlert />, text: "Catálogo de códigos de error y guías de 'troubleshooting' para resolver fallos rápidamente." },
  ];

  return (
    <div className="app-container">
      <nav className="navbar">
        <div className="logo">Transpiler_DOC</div>
      </nav>
      
      <main className="main-content">
        <header className="hero">
          <h1 className="hero-title">Plan de Documentación</h1>
          <p className="hero-subtitle">Hojas de ruta y tareas para el equipo técnico</p>
        </header>

        <section className="grid-container">
          {docPoints.map((point, index) => (
            <div key={index} className="card">
              <div className="card-border"></div>
              <div className="card-content">
                <div className="card-header">
                  <span className="card-icon">{point.icon}</span>
                  <h3 className="card-title">{point.title}</h3>
                </div>
                <p className="card-text">{point.text}</p>
                <a href="#" className="card-link">Leer más →</a>
              </div>
            </div>
          ))}
        </section>
      </main>

      <footer className="footer">
        <p>© 2024 Transpiler Project. Todos los derechos reservados.</p>
      </footer>
    </div>
  )
}

export default App