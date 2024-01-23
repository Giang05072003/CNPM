import './App.css';
import { ThemeProvider, useTheme } from '@emotion/react';
import { Navigate, Route, Routes } from 'react-router-dom';
import Admin from './Components/Layout/Admin';

function App() {
    const theme = useTheme();
    return (
        <ThemeProvider theme={theme}>
            <Routes>
                <Route path="/" element={<Navigate to="/admin" />} />
                <Route path="/admin/*" element={<Admin />} />
            </Routes>
        </ThemeProvider>
    );
}

export default App;
