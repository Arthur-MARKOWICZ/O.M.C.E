import { Navigate, Route, Routes } from 'react-router-dom';
import Layout from '../components/layout/Layout';
import { Guest, Protected, RequireRole } from '../components/layout/RouteGuards';
import Home from '../pages/Home';
import Login from '../pages/auth/Login';
import NewPassword from '../pages/auth/NewPassword';
import PasswordReset from '../pages/auth/PasswordReset';
import Register from '../pages/auth/Register';
import Cart from '../pages/cart/Cart';
import Checkout from '../pages/cart/Checkout';
import History from '../pages/history/History';
import Feed from '../pages/product/Feed';
import ProductDetail from '../pages/product/ProductDetail';
import ProductForm from '../pages/product/ProductForm';
import MyProducts from '../pages/product/MyProducts';
import ProductReviews from '../pages/reviews/ProductReviews';
import SellerReviewForm from '../pages/reviews/SellerReviewForm';
import SellerReviews from '../pages/reviews/SellerReviews';
import Profile from '../pages/user/Profile';
import AdminDashboard from '../pages/admin/AdminDashboard';
import AdminUsuarios from '../pages/admin/AdminUsuarios';
import AdminProdutos from '../pages/admin/AdminProdutos';

function GuestRoute({ children }) { return <Guest>{children}</Guest>; }

function PrivateRoutes() {
  return <Layout><Routes>
    <Route index element={<Home />} />
    <Route path="feed" element={<Feed />} />
    <Route path="produto/novo" element={<RequireRole roles={['VENDEDOR']}><ProductForm /></RequireRole>} />
    <Route path="produto/:id/editar" element={<RequireRole roles={['VENDEDOR']}><ProductForm edit /></RequireRole>} />
    <Route path="produto/:id" element={<ProductDetail />} />
    <Route path="carrinho" element={<RequireRole roles={['COMPRADOR']}><Cart /></RequireRole>} />
    <Route path="pedido" element={<RequireRole roles={['COMPRADOR']}><Checkout /></RequireRole>} />
    <Route path="minha-conta" element={<Profile />} />
    <Route path="meus-produtos" element={<RequireRole roles={['VENDEDOR']}><MyProducts /></RequireRole>} />
    <Route path="historico/:type" element={<History />} />
    <Route path="avaliacoes" element={<RequireRole roles={['VENDEDOR']}><SellerReviews /></RequireRole>} />
    <Route path="produto/:id/avaliacoes" element={<ProductReviews />} />
    <Route path="vendedor/:id/avaliar" element={<SellerReviewForm />} />
    <Route path="admin" element={<RequireRole roles={['ADMIN']}><AdminDashboard /></RequireRole>} />
    <Route path="admin/usuarios" element={<RequireRole roles={['ADMIN']}><AdminUsuarios /></RequireRole>} />
    <Route path="admin/produtos" element={<RequireRole roles={['ADMIN']}><AdminProdutos /></RequireRole>} />
    <Route path="*" element={<Navigate to="/" replace />} />
  </Routes></Layout>;
}

export default function AppRoutes() {
  return <Routes>
    <Route path="/login" element={<GuestRoute><Login /></GuestRoute>} />
    <Route path="/cadastro" element={<GuestRoute><Register /></GuestRoute>} />
    <Route path="/redefinir-senha" element={<GuestRoute><PasswordReset /></GuestRoute>} />
    <Route path="/nova-senha" element={<GuestRoute><NewPassword /></GuestRoute>} />
    <Route path="/*" element={<Protected><PrivateRoutes /></Protected>} />
  </Routes>;
}