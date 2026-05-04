export type OrderStatus =
  | 'RECEIVED'
  | 'IN_DIAGNOSIS'
  | 'WAITING_FOR_PARTS'
  | 'READY_FOR_PICKUP'
  | 'DELIVERED'
  | 'CANCELLED';

export const ORDER_STATUS_LABELS: Record<OrderStatus, string> = {
  RECEIVED: 'Recibida',
  IN_DIAGNOSIS: 'En Diagnóstico',
  WAITING_FOR_PARTS: 'Esperando Repuestos',
  READY_FOR_PICKUP: 'Lista para Retirar',
  DELIVERED: 'Entregada',
  CANCELLED: 'Cancelada',
};

export interface Photo {
  id: number;
  fileName: string;
  filePath: string;
  uploadedAt: string;
}

export interface Order {
  id: string;
  clientName: string;
  clientPhone: string;
  clientEmail?: string;
  deviceType: string;
  deviceBrand?: string;
  deviceModel?: string;
  issueDescription: string;
  status: OrderStatus;
  statusLabel: string;
  technicianNotes?: string;
  estimatedCost?: number;
  finalCost?: number;
  photos: Photo[];
  createdAt: string;
  updatedAt: string;
}

export interface CreateOrderRequest {
  clientName: string;
  clientPhone: string;
  clientEmail?: string;
  deviceType: string;
  deviceBrand?: string;
  deviceModel?: string;
  issueDescription: string;
}

export interface UpdateOrderRequest {
  clientName?: string;
  clientPhone?: string;
  clientEmail?: string;
  deviceType?: string;
  deviceBrand?: string;
  deviceModel?: string;
  issueDescription?: string;
  technicianNotes?: string;
  estimatedCost?: number;
  finalCost?: number;
}

export interface StatusTransitionRequest {
  newStatus: OrderStatus;
}

export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface ErrorResponse {
  status: number;
  message: string;
  timestamp: string;
}

export interface ValidationError {
  status: number;
  message: string;
  errors: Record<string, string>;
  timestamp: string;
}