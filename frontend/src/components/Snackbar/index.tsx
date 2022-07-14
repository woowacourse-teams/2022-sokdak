import ReactDOM from 'react-dom';

import * as Styled from './index.styles';

interface SnackbarProps {
  message: string;
}

const Snackbar = ({ message }: SnackbarProps) => {
  return (
    <>{ReactDOM.createPortal(<Styled.Container>{message}</Styled.Container>, document.getElementById('snackbar')!)}</>
  );
};

export default Snackbar;
