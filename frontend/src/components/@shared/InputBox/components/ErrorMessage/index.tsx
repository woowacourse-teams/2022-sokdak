import * as Styled from './index.styles';

import { useInputContext } from '../../useInputContext';

const ErrorMessage = () => {
  const { error } = useInputContext();
  return error ? <Styled.Message>{error}</Styled.Message> : <></>;
};

export default ErrorMessage;
