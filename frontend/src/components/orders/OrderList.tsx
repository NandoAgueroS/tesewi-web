import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Order, OrderStatus } from '../../types/order';
import { orderApi } from '../../api/order';

export function OrderList() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    loadOrders();
  }, [page]);

  const loadOrders = async () => {
    try {
      setLoading(true);
      const result = await orderApi.getOrders(page, 20, 'createdAt', 'desc');
      setOrders(result.content);
      setTotalPages(result.totalPages);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load orders');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status: OrderStatus): string => {
    const colors: Record<OrderStatus, string> = {
      RECEIVED: '#3b82f6',
      IN_DIAGNOSIS: '#f59e0b',
      WAITING_FOR_PARTS: '#8b5cf6',
      READY_FOR_PICKUP: '#10b981',
      DELIVERED: '#22c55e',
      CANCELLED: '#ef4444',
    };
    return colors[status] || '#6b7280';
  };

  if (loading) return <div className="p-4">Loading...</div>;
  if (error) return <div className="p-4 text-red-500">Error: {error}</div>;

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">Órdenes de Servicio</h1>
      
      <div className="overflow-x-auto">
        <table className="min-w-full bg-white border border-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-2 text-left text-sm font-semibold text-gray-600">ID</th>
              <th className="px-4 py-2 text-left text-sm font-semibold text-gray-600">Cliente</th>
              <th className="px-4 py-2 text-left text-sm font-semibold text-gray-600">Dispositivo</th>
              <th className="px-4 py-2 text-left text-sm font-semibold text-gray-600">Estado</th>
              <th className="px-4 py-2 text-left text-sm font-semibold text-gray-600">Fecha</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order) => (
              <tr 
              key={order.id} 
              className="border-t border-gray-200 hover:bg-gray-50 cursor-pointer"
              onClick={() => navigate(`/order/${order.id}`)}
            >
                <td className="px-4 py-2 text-sm">{order.id}</td>
                <td className="px-4 py-2 text-sm">
                  <div className="font-medium">{order.clientName}</div>
                  <div className="text-gray-500 text-xs">{order.clientPhone}</div>
                </td>
                <td className="px-4 py-2 text-sm">
                  <div>{order.deviceType}</div>
                  {order.deviceBrand && (
                    <div className="text-gray-500 text-xs">
                      {order.deviceBrand} {order.deviceModel}
                    </div>
                  )}
                </td>
                <td className="px-4 py-2">
                  <span
                    className="inline-block px-2 py-1 text-xs font-medium rounded-full text-white"
                    style={{ backgroundColor: getStatusColor(order.status) }}
                  >
                    {order.statusLabel}
                  </span>
                </td>
                <td className="px-4 py-2 text-sm text-gray-500">
                  {new Date(order.createdAt).toLocaleDateString('es-AR')}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {totalPages > 1 && (
        <div className="mt-4 flex justify-center gap-2">
          <button
            onClick={() => setPage((p) => Math.max(0, p - 1))}
            disabled={page === 0}
            className="px-3 py-1 border rounded disabled:opacity-50"
          >
            Anterior
          </button>
          <span className="px-3 py-1">
            Página {page + 1} de {totalPages}
          </span>
          <button
            onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
            disabled={page >= totalPages - 1}
            className="px-3 py-1 border rounded disabled:opacity-50"
          >
            Siguiente
          </button>
        </div>
      )}
    </div>
  );
}