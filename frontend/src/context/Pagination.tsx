import { StateAndAction } from 'sokdak-util-types';

import React, { PropsWithChildren, useState } from 'react';

const PaginationContext = React.createContext<StateAndAction<number, 'page'>>({} as StateAndAction<number, 'page'>);

export const PaginationContextProvider = ({ children }: PropsWithChildren) => {
  const [page, setPage] = useState(1);

  return <PaginationContext.Provider value={{ page, setPage }}>{children}</PaginationContext.Provider>;
};

export default PaginationContext;
