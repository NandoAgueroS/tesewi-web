import { useState, useEffect } from 'react';
import { Order, OrderStatus, ORDER_STATUS_LABELS } from '../../types/order';
import { orderApi } from '../../api/order';

interface OrderDetailProps {
  orderId: string;
  onBack?: () => void;
}

export function OrderDetail({ orderId, onBack }: OrderDetailProps) {
  const [order, setOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [updating, setUpdating] = useState(false);

  useEffect(() => {
    loadOrder();
  }, [orderId]);

  const loadOrder = async () => {
    try {
      setLoading(true);
      const data = await orderApi.getOrderById(orderId);
      setOrder(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load order');
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

  const validTransitions: Record<OrderStatus, OrderStatus[]> = {
    RECEIVED: ['IN_DIAGNOSIS', 'CANCELLED'],
    IN_DIAGNOSIS: ['WAITING_FOR_PARTS', 'READY_FOR_PICKUP', 'CANCELLED'],
    WAITING_FOR_PARTS: ['READY_FOR_PICKUP', 'CANCELLED'],
    READY_FOR_PICKUP: ['DELIVERED', 'CANCELLED'],
    DELIVERED: [],
    CANCELLED: [],
  };

  const handleStatusChange = async (newStatus: OrderStatus) => {
    if (!order) return;
    setUpdating(true);
    try {
      const updated = await orderApi.transitionStatus(order.id, { newStatus });
      setOrder(updated);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update status');
    } finally {
      setUpdating(false);
    }
  };

  if (loading) return <div className="p-4">Cargando...</div>;
  if (error) return <div className="p-4 text-red-500">Error: {error}</div>;
  if (!order) return <div className="p-4">Orden no encontrada</div>;

  const availableTransitions = validTransitions[order.status];

  return (
    <div className="p-4 max-w-3xl">
      {onBack && (
        <button
          onClick={onBack}
          className="mb-4 text-blue-600 hover:underline"
        >
          ← Volver
        </button>
      )}

      <div className="bg-white rounded shadow p-6">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h1 className="text-2xl font-bold">{order.id}</h1>
            <span
              className="inline-block px-3 py-1 text-sm font-medium rounded-full text-white mt-2"
              style={{ backgroundColor: getStatusColor(order.status) }}
            >
              {order.statusLabel}
            </span>
          </div>
          <div className="text-right text-sm text-gray-500">
            <div>Creado: {new Date(order.createdAt).toLocaleString('es-AR')}</div>
            <div>Actualizado: {new Date(order.updatedAt).toLocaleString('es-AR')}</div>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <h3 className="font-semibold text-gray-600 mb-2">Cliente</h3>
            <p className="font-medium">{order.clientName}</p>
            <p className="text-gray-600">{order.clientPhone}</p>
            {order.clientEmail && (
              <p className="text-gray-600">{order.clientEmail}</p>
            )}
          </div>

          <div>
            <h3 className="font-semibold text-gray-600 mb-2">Dispositivo</h3>
            <p className="font-medium">{order.deviceType}</p>
            {order.deviceBrand && <p className="text-gray-600">Marca: {order.deviceBrand}</p>}
            {order.deviceModel && <p className="text-gray-600">Modelo: {order.deviceModel}</p>}
          </div>

          <div className="md:col-span-2">
            <h3 className="font-semibold text-gray-600 mb-2">Descripción del Problema</h3>
            <p className="text-gray-800">{order.issueDescription}</p>
          </div>

          {order.technicianNotes && (
            <div className="md:col-span-2">
              <h3 className="font-semibold text-gray-600 mb-2">Notas del Técnico</h3>
              <p className="text-gray-800">{order.technicianNotes}</p>
            </div>
          )}

          {(order.estimatedCost || order.finalCost) && (
            <div className="md:col-span-2">
              <h3 className="font-semibold text-gray-600 mb-2">Costos</h3>
              {order.estimatedCost && (
                <p className="text-gray-600">Estimado: ${order.estimatedCost}</p>
              )}
              {order.finalCost && (
                <p className="font-medium">Final: ${order.finalCost}</p>
              )}
            </div>
          )}
        </div>

        {availableTransitions.length > 0 && (
          <div className="mt-6 pt-6 border-t">
            <h3 className="font-semibold text-gray-600 mb-3">Cambiar Estado</h3>
            <div className="flex gap-2 flex-wrap">
              {availableTransitions.map((status) => (
                <button
                  key={status}
                  onClick={() => handleStatusChange(status)}
                  disabled={updating}
                  className="px-3 py-1 text-sm border rounded hover:bg-gray-100 disabled:opacity-50"
                >
                  {ORDER_STATUS_LABELS[status]}
                </button>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}