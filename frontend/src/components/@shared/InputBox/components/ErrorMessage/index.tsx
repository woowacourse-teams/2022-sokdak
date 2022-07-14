import * as Styled from './index.styles';

import { useInputContext } from '../../useInputContext';

const ErrorMessage = () => {
  const { error } = useInputContext();
  return <Styled.MessageContainer>{error ? <Styled.Message>{error}</Styled.Message> : null}</Styled.MessageContainer>;
};

export default ErrorMessage;
