import {
  Order,
  CreateOrderRequest,
  UpdateOrderRequest,
  StatusTransitionRequest,
  Page,
  OrderStatus,
  ValidationError,
} from '../types/order';

// Usar variables de entorno (Vite)
const API_BASE = import.meta.env.VITE_API_BASE_URL + import.meta.env.VITE_API_ORDERS_PATH;

class OrderApi {
  private handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
      return response.json().then((error) => {
        if (response.status === 400) {
          const validationError = error as ValidationError;
          throw new Error(
            validationError.errors
              ? Object.values(validationError.errors).join(', ')
              : validationError.message
          );
        }
        throw new Error(error.message || 'An error occurred');
      });
    }
    return response.json();
  }

  async createOrder(data: CreateOrderRequest): Promise<Order> {
    const response = await fetch(API_BASE, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    return this.handleResponse<Order>(response);
  }

  async getOrders(
    page = 0,
    size = 20,
    sortBy = 'createdAt',
    sortDir = 'desc'
  ): Promise<Page<Order>> {
    const params = new URLSearchParams({
      page: String(page),
      size: String(size),
      sortBy,
      sortDir,
    });
    const response = await fetch(`${API_BASE}?${params}`);
    return this.handleResponse<Page<Order>>(response);
  }

  async getOrderById(id: string): Promise<Order> {
    const response = await fetch(`${API_BASE}/${id}`);
    return this.handleResponse<Order>(response);
  }

  async updateOrder(id: string, data: UpdateOrderRequest): Promise<Order> {
    const response = await fetch(`${API_BASE}/${id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    return this.handleResponse<Order>(response);
  }

  async transitionStatus(id: string, data: StatusTransitionRequest): Promise<Order> {
    const response = await fetch(`${API_BASE}/${id}/status`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    return this.handleResponse<Order>(response);
  }

  async getValidStatuses(): Promise<OrderStatus[]> {
    const response = await fetch(`${API_BASE}/statuses`);
    return this.handleResponse<OrderStatus[]>(response);
  }
}

export const orderApi = new OrderApi();