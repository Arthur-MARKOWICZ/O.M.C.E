---
name: react-ux-ui-best-practices
description: Use when building or modifying React front-end in projetos/front-end-react — components, pages, routes, API integration, styling, theming or UX flows.
---

# React + UX/UI Best Practices — O.M.C.E

Applies to `projetos/front-end-react/` (React + Vite + `pnpm`, `react-router-dom`, design in `src/styles.css`).

## 1. Project wiring — do not diverge

- Entry: `src/main.jsx:6` → `src/app/App.jsx` → `src/routes/AppRoutes.jsx`. Never create a second entry or router.
- API: **only** `src/api.js:request()` for HTTP. It injects `Authorization: Bearer <jwt>` from `localStorage.jwt`, sets `Content-Type: application/json` when body exists, and throws `Error(body.mensagem || body.message || ...)`. Never use bare `fetch` to the API in components.
- Auth storage: `src/api.js:auth` (`jwt`, `id_usuario`, `nome` in `localStorage`). Use `auth.loggedIn()`, `auth.save({token,id,nome})`, `auth.clear()`. Cart is per-user: `carrinho_${id_usuario}` via `getCart()/saveCart()`.
- Config: `VITE_API_URL` (`src/api.js:1` → `import.meta.env.VITE_API_URL || 'http://localhost:8080'`). Never hardcode URLs.
- State: `src/contexts/ThemeContext.jsx` + `src/contexts/NoticeContext.jsx` provided in `App.jsx`. Theme toggles `document.documentElement.dataset.theme` + `localStorage.theme` (`dark`/`light`, respects `prefers-color-scheme`).

## 2. Routing and guards

- All routes declared in `src/routes/AppRoutes.jsx:28`. Public (guest-only): `/login`, `/cadastro`, `/redefinir-senha`, `/nova-senha` wrapped in `<Guest>`. Private: `/*` wrapped in `<Protected>` → `<Layout>` → inner `<Routes>`.
- Guards: `src/components/layout/RouteGuards.jsx:3` — `Guest` redirects logged users to `/`, `Protected` redirects anonymous to `/login`. Preserve this pattern for new routes.
- Add new pages inside `PrivateRoutes` unless explicitly public. Use `useNavigate` + `request()` for navigation after mutations.

## 3. Component structure

```
src/app/App.jsx              → providers + BrowserRouter + toast
src/routes/AppRoutes.jsx     → route table
src/components/layout/       → Layout.jsx (topbar), RouteGuards.jsx, ThemeToggle.jsx, AuthShell.jsx
src/components/product/      → ProductCard.jsx, ProductGrid.jsx, HistoryItem.jsx, ReviewsList.jsx
src/components/ui/           → Feedback.jsx (Empty/Loading/ErrorMessage), Page.jsx, Pagination.jsx
src/pages/<domain>/          → Feed.jsx, ProductDetail.jsx, ProductForm.jsx, etc.
src/contexts/                → ThemeContext.jsx, NoticeContext.jsx
src/utils/fileToData.js      → image file → {data, type} (base64)
```

- Keep presentational components in `components/`, page orchestration (data fetching + layout) in `pages/`. Do not put `request()` calls inside `components/product/*` unless it's the grid/card itself needs it — prefer pages to fetch and pass props.

## 4. Data fetching pattern

Canonical (`src/pages/product/Feed.jsx:9`):
```jsx
const [products, setProducts] = useState([]);
const [result, setResult] = useState(null); // Page object from API
const [error, setError] = useState('');
useEffect(() => {
  const query = new URLSearchParams({ page });
  Object.entries(filters).forEach(([k,v]) => v && query.set(k, v));
  setResult(null);
  request(`/produto/filtro?${query}`)
    .then(data => { setProducts(data.content || []); setResult(data); })
    .catch(e => setError(e.message));
}, [page, filters]);
```

- Render states: `error → <ErrorMessage>`, `result === null → <Loading>`, `products.length === 0 → <Empty>`, else grid + `<Pagination>`.
- Forms: `Object.fromEntries(new FormData(event.currentTarget))` → build payload → `await request(path, {method, body: JSON.stringify(payload)})`. Show saving/loading via `disabled` button (`ProductForm.jsx:11`).

## 5. Styling and design system (`src/styles.css:3`)

Tokens (do not invent new palette without reason):
```
--ink #153128 / #ecf5ef (dark)
--green #176b42 (primary action), --green-dark #0d492c, --lime #d3ea5a (accent)
--paper #fbfcf9 / #101713, --surface #fff / #19231e, --line #dbe5df / #30443b
--danger #b53333, --muted #6b7a73
Fonts: Manrope (UI), DM Mono (eyebrow/labels), Playfair Display (headings)
```

- Use existing classes: `.page`, `.page-heading`, `.eyebrow`, `.button.primary/.secondary`, `.filters`, `.product-grid`, `.product-card`, `.form-card`, `.feedback.error/.success`, `.toast`, `.empty`, `.loading`, `.pagination`, `.topbar`, `.hero`.
- Dark mode: never hardcode colors; use `var(--ink)`, `var(--surface)`, `var(--line)`, etc. Test both `html[data-theme='dark']` and light.
- Responsive breakpoints already in `styles.css:230` (`880px`, `620px`). New layouts must respect `.page { max-width:1180px }` and grid collapses (`product-grid: 3→2→1`).

## 6. UX / UI guidelines

- **Feedback:** Every mutation needs success/error feedback. Use `useNotice()` toast (`App.jsx:12` → `notice({message, type})`) for success, `Feedback.jsx:ErrorMessage` inline for form errors. Never use `alert()`.
- **Forms:** Required fields marked `required`; price `type="number" min="0.01" step="0.01"`; select with disabled placeholder `<option value="" disabled>Selecione</option>`; file inputs with `accept="image/*"`. Keep Portuguese labels/messages (`mensagem`, `Escolha uma imagem...`).
- **Images:** Backend expects `imagem` (base64 without prefix) + `imagem_tipo` (MIME). Use `utils/fileToData.js` to convert `File` → `{data, type}`. Display via `imageSource(product)` (`api.js:29` handles `imagem`/`Imagem` + `imagem_tipo`/`Imagem_tipo` + `data:` URI).
- **Money:** `money(value)` (`api.js:35` → `toLocaleString('pt-BR', {style:'currency', currency:'BRL'})`). Never format manually.
- **Empty/loading:** Use `<Empty title text action>` and `<Loading>` from `components/ui/Feedback.jsx` — keeps tone consistent (`product-grid` pattern).
- **Pagination:** `<Pagination page result onPage>` (`result` is Spring `Page` with `totalPages`, `number`). Do not reinvent.
- **Cart UX:** After add/remove dispatch `window.dispatchEvent(new Event('cart-updated'))` so `Layout.jsx:10` badge updates; cart key is per-user.
- **Accessibility:** `aria-label` on icon buttons (`Layout.jsx:15` cart), `role="status"` on toast, `label` wraps `input/select` with text, `focus` ring via `input:focus { border-color: var(--green); box-shadow }`.

## 7. Theming

- Toggle: `src/components/layout/ThemeToggle.jsx` with `useTheme()`; auth pages use `.auth-theme-toggle` positioned top-right. Toggle applies `document.documentElement.dataset.theme = 'dark'|'light'` and `style.colorScheme`.
- All new surfaces must use `var(--surface)` / `var(--surface-muted)` and adapt in `html[data-theme='dark']` block (`styles.css:225`). Test hover states in both themes.

## 8. Auth and navigation

- Login saves `jwt` + `id_usuario` + `nome` via `auth.save`; `Layout.jsx:13` shows `auth.name`, logout is `auth.clear(); navigate('/login')`.
- Protected API calls automatically add `Authorization` header via `request()`. For user-scoped `GET`s the backend also expects `Id-Usuario` header — pass `headers: { 'Id-Usuario': auth.userId }` when needed (`mobile/src/lib/api.ts:api.myProducts` pattern, same in React pages that call `/produto/todosProdutosUsuario`).
- Cart helpers: `cartKey()` depends on `auth.userId` — never call before login check.

## 9. Validation

- `VITE_API_URL` is dev-only public; never put secrets in it. No test suite for React — validation is `pnpm build` (`projetos/front-end-react/package.json:8` → `vite build`). Run `pnpm build` before commit to catch broken imports/types. Docker build uses `node:22-alpine` + `pnpm install --frozen-lockfile`.

## Checklist before commit

- [ ] Uses `src/api.js:request()` (and `money`/`imageSource`/`getCart`) — no ad-hoc fetch
- [ ] Route added in `AppRoutes.jsx` with correct `Guest`/`Protected` wrapper
- [ ] Loading/error/empty states handled with `Feedback.jsx` + `NoticeContext` toast
- [ ] Styles use existing tokens/classes, dark mode verified, responsive at 880/620px
- [ ] Portuguese copy, `pt-BR` currency, `data:` image handling correct
- [ ] `pnpm build` passes
