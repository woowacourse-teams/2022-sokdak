import SearchIcon from '@/assets/images/search.svg';

import styled from '@emotion/styled';

export const Search = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  color: ${props => props.theme.colors.gray_150};
  border: 1px solid ${props => props.theme.colors.gray_150};
  padding: 1em;
  border-radius: 20px;
  cursor: text;
`;

export const SearchImg = styled(SearchIcon)`
  cursor: pointer;
  margin: 8px;
  overflow: visible;
`;
