import { CssBaseline } from '@mui/material';
import { SnackbarProvider } from 'notistack';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import App from './App';
import './index.css';
const Container = () => {
    return (
            <BrowserRouter>
                <SnackbarProvider autoHideDuration={2000} anchorOrigin={{ horizontal: 'right', vertical: 'top' }}>
                    <CssBaseline />
                    <App />
                </SnackbarProvider>
            </BrowserRouter>
    );
};
ReactDOM.createRoot(document.getElementById('root')!).render(<Container />);
