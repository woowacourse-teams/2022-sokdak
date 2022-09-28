import React, { Dispatch, PropsWithChildren, SetStateAction, useState } from 'react';

interface PaginationContextValue {
  page: number;
  setPage: Dispatch<SetStateAction<number>>;
}

const PaginationContext = React.createContext<PaginationContextValue>({} as PaginationContextValue);

export const PaginationContextProvider = ({ children }: PropsWithChildren) => {
  const [page, setPage] = useState(1);

  return <PaginationContext.Provider value={{ page, setPage }}>{children}</PaginationContext.Provider>;
};

export default PaginationContext;
