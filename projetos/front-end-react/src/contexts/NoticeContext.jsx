import { createContext, useContext } from 'react';

export const NoticeContext = createContext(() => {});
export const useNotice = () => useContext(NoticeContext);
