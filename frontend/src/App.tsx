import { BrowserRouter, Routes, Route, Link, useParams } from 'react-router-dom';
import { OrderList } from './components/orders/OrderList';
import { OrderForm } from './components/orders/OrderForm';
import { OrderDetail } from './components/orders/OrderDetail';

function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-gray-100">
        <header className="bg-blue-600 text-white p-4 shadow">
          <div className="container mx-auto flex justify-between items-center">
            <h1 className="text-xl font-bold">Tesewi - Órdenes de Servicio</h1>
            <nav className="flex gap-4">
              <Link to="/" className="hover:underline">Lista</Link>
              <Link to="/new" className="hover:underline">Nueva Orden</Link>
            </nav>
          </div>
        </header>

        <main className="container mx-auto py-4">
          <Routes>
            <Route path="/" element={<OrderList />} />
            <Route path="/new" element={<OrderForm />} />
            <Route path="/order/:id" element={<OrderDetailWrapper />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

function OrderDetailWrapper() {
  const { id } = useParams<{ id: string }>();
  return <OrderDetail orderId={id || ''} />;
}

export default App;