import SearchImage from '@/assets/images/search.svg';

import styled from '@emotion/styled';

export const Search = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  color: ${props => props.theme.colors.gray_400};
  border: 1px solid ${props => props.theme.colors.gray_400};
  height: 2.2rem;
  border-radius: 20px;
  cursor: text;
  flex-grow: 2;
  max-width: 700px;
`;

export const SearchIcon = styled(SearchImage)`
  cursor: pointer;
  margin: 10px 15px;
  overflow: visible;
`;

export const Placeholder = styled.p`
  font-size: 0.8rem;
  color: ${props => props.theme.colors.gray_200};
  padding-top: 2px;
`;
