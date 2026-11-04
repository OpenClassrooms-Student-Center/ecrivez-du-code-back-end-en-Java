// Données simulées, écrites en dur en attendant le branchement de l'API.
import { LeaveApiError } from './leave-api-error.js';

const requests = [
  { id: 1, employeeId: 1, startDate: '2026-07-06', endDate: '2026-07-10', days: 5, status: 'PENDING' },
  { id: 2, employeeId: 2, startDate: '2026-05-18', endDate: '2026-05-20', days: 3, status: 'APPROVED' },
  { id: 3, employeeId: 3, startDate: '2026-08-03', endDate: '2026-08-07', days: 5, status: 'REFUSED' },
  { id: 4, employeeId: 1, startDate: '2026-09-14', endDate: '2026-09-15', days: 2, status: 'PENDING' },
];

export async function getBalance(employeeId) {
  if (!(employeeId > 0)) {
    throw new LeaveApiError('Salarié introuvable.');
  }
  return 12.5;
}

export async function getRequests(status) {
  return requests
    .filter((request) => !status || request.status === status)
    .map((request) => ({ ...request }));
}

export async function createRequest(newRequest) {
  const { employeeId, startDate, endDate } = newRequest;
  if (!startDate || !endDate) {
    throw new LeaveApiError('Les dates de début et de fin sont obligatoires.');
  }
  if (endDate < startDate) {
    throw new LeaveApiError('La date de fin doit suivre la date de début.');
  }
  const days = (new Date(endDate) - new Date(startDate)) / 86_400_000 + 1;
  const created = {
    id: Math.max(...requests.map((request) => request.id)) + 1,
    employeeId: Number(employeeId),
    startDate,
    endDate,
    days,
    status: 'PENDING',
  };
  requests.push(created);
  return { ...created };
}

export async function approve(requestId) {
  return decide(requestId, 'APPROVED');
}

export async function refuse(requestId) {
  return decide(requestId, 'REFUSED');
}

function decide(requestId, status) {
  const request = requests.find((candidate) => candidate.id === requestId);
  if (!request) {
    throw new LeaveApiError('Demande introuvable.');
  }
  if (request.status !== 'PENDING') {
    throw new LeaveApiError('Cette demande a déjà été traitée.');
  }
  request.status = status;
  return { ...request };
}
