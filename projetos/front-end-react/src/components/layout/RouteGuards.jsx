import { Navigate } from 'react-router-dom';
import { auth } from '../../api';

export function Guest({ children }) { return auth.loggedIn() ? <Navigate to="/" replace /> : children; }
export function Protected({ children }) { return auth.loggedIn() ? children : <Navigate to="/login" replace />; }

export function RequireRole({ roles, children }) {
  if (!auth.loggedIn()) {
    return <Navigate to="/login" replace />;
  }

  const role = auth.role;

  if (!roles) {
    return children;
  }

  return roles.includes(role)
      ? children
      : <Navigate to="/feed" replace />;
}

export function RequireVendedor({ children }) {
  return <RequireRole roles={['VENDEDOR']}>{children}</RequireRole>;
}

export function RequireComprador({ children }) {
  return <RequireRole roles={['COMPRADOR', 'VENDEDOR']}>{children}</RequireRole>;
}
