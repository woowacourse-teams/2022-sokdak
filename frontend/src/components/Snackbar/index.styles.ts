import styled from '@emotion/styled';

export const Container = styled.div`
  width: 300px;
  height: 50px;
  background-color: ${props => props.theme.colors.gray_300};
  border-radius: 7px;
  color: white;
  font-size: 11px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  -webkit-animation: fadein 0.5s, fadeout 0.5s 1.2s;
  animation: fadein 0.5s, fadeout 0.5s 1.2s;

  @-webkit-keyframes fadein {
    from {
      bottom: 0;
      opacity: 0;
    }
    to {
      bottom: 30px;
      opacity: 1;
    }
  }
  @keyframes fadein {
    from {
      bottom: 0;
      opacity: 0;
    }
    to {
      bottom: 30px;
      opacity: 1;
    }
  }
  @-webkit-keyframes fadeout {
    from {
      bottom: 30px;
      opacity: 1;
    }
    to {
      bottom: 0;
      opacity: 0;
    }
  }
  @keyframes fadeout {
    from {
      bottom: 30px;
      opacity: 1;
    }
    to {
      bottom: 0;
      opacity: 0;
    }
  }
`;
