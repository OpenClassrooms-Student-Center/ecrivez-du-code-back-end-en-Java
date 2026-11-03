import { LeaveApiError } from './leave-api-error.js';

const baseUrl = document.querySelector('meta[name="api-base"]').content;

export async function getBalance(employeeId) {
  const balance = await send(`employees/${employeeId}/balance`);
  return balance.balance;
}

export async function getRequests(status) {
  const query = status ? `?status=${encodeURIComponent(status)}` : '';
  const requests = await send(`leave-requests${query}`);
  return requests.map(toLeaveRequest);
}

export async function createRequest(newRequest) {
  const created = await send('leave-requests', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(newRequest),
  });
  return toLeaveRequest(created);
}

export async function approve(requestId) {
  return toLeaveRequest(await send(`leave-requests/${requestId}/approval`, { method: 'POST' }));
}

export async function refuse(requestId) {
  return toLeaveRequest(await send(`leave-requests/${requestId}/refusal`, { method: 'POST' }));
}

function toLeaveRequest(dto) {
  return {
    id: dto.id,
    employeeId: dto.employeeId,
    startDate: dto.startDate,
    endDate: dto.endDate,
    days: dto.workingDays,
    status: dto.status,
  };
}

async function send(path, options = {}) {
  const response = await fetch(baseUrl + path, options);
  if (response.ok) {
    return response.json();
  }
  throw new LeaveApiError(await readErrorMessage(response));
}

async function readErrorMessage(response) {
  const fallback = `Erreur ${response.status}`;
  const contentType = response.headers.get('Content-Type') ?? '';
  if (!contentType.includes('application/problem+json')) {
    return fallback;
  }
  const problem = await response.json();
  return Object.values(problem.errors ?? {})[0] ?? problem.detail ?? fallback;
}
