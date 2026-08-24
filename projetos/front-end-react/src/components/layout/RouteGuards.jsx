import { Navigate } from 'react-router-dom';
import { auth } from '../../api';

export function Guest({ children }) { return auth.loggedIn() ? <Navigate to="/" replace /> : children; }
export function Protected({ children }) { return auth.loggedIn() ? children : <Navigate to="/login" replace />; }
