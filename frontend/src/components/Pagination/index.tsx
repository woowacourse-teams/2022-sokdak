import { useEffect, useState } from 'react';

import * as Styled from './index.styles';

const MAX_PAGE_LOAD = 8;
const STANDARD = MAX_PAGE_LOAD / 2;
const DISPLAY_STANDARD = STANDARD + 1;

const Pagination = () => {
  const firstPage = 1;
  const lastPage = 11;
  const pages = Array.from({ length: lastPage }, (_, i) => i + 1);
  const [currentPage, setCurrentPage] = useState(firstPage);
  const [currentPages, setCurrentPages] = useState<(number | string)[]>(pages);

  const handleClick = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    const page = Number(e.currentTarget.innerText);

    if (isNaN(page)) return;

    setCurrentPage(page);
  };

  useEffect(() => {
    switch (true) {
      case lastPage < MAX_PAGE_LOAD:
        break;

      case lastPage - currentPage >= STANDARD && currentPage - firstPage >= STANDARD:
        setCurrentPages([firstPage, '...', currentPage - 1, currentPage, currentPage + 1, '...', lastPage]);
        break;

      case lastPage - currentPage >= STANDARD:
        setCurrentPages([...pages.slice(0, DISPLAY_STANDARD), '...', lastPage]);
        break;

      case currentPage - firstPage >= STANDARD:
        setCurrentPages([firstPage, '...', ...pages.slice(-DISPLAY_STANDARD)]);
        break;
    }
  }, [currentPage]);

  return (
    <Styled.Container>
      {currentPages.map((page, index) => (
        <Styled.Page key={index} onClick={handleClick} isCurrentPage={page === currentPage}>
          {page}
        </Styled.Page>
      ))}
    </Styled.Container>
  );
};

export default Pagination;
