import { request, setApiToken } from '@/src/lib/api';

describe('API client', () => {
  afterEach(() => { jest.restoreAllMocks(); setApiToken(null); });
  it('sends JSON and authentication headers', async () => {
    setApiToken('token-123');
    const fetchMock = jest.spyOn(globalThis, 'fetch').mockResolvedValue({ ok: true, headers: { get: () => 'application/json' }, json: async () => ({ ok: true }) } as unknown as Response);
    await expect(request<{ ok: boolean }>('/status', { method: 'POST', body: JSON.stringify({ check: true }) })).resolves.toEqual({ ok: true });
    expect(fetchMock.mock.calls[0][1]?.headers).toEqual(expect.any(Headers));
    const headers = fetchMock.mock.calls[0][1]?.headers as Headers;
    expect(headers.get('Authorization')).toBe('Bearer token-123');
    expect(headers.get('Content-Type')).toBe('application/json');
  });
});
