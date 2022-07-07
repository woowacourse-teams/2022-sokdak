import styled from '@emotion/styled';

export const PostForm = styled.form`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  padding: 0 13px;
  box-sizing: border-box;
`;

export const SpinnerContainer = styled.div`
  width: 100%;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const Heading = styled.h1`
  font-family: 'BMHANNAPro';
  font-size: 27px;
  margin: 40px 0;
`;

export const TitleInput = styled.input`
  font-family: 'BMHANNAAir';
  border-bottom: 1px solid ${props => props.theme.colors.sub};
  width: 100%;
  padding: 10px;
  font-size: 20px;
`;

export const ContentInput = styled.textarea`
  width: 100%;
  height: 290px;
  padding: 10px;
  font-size: 14px;
  margin: 20px 0;
`;

export const PostButton = styled.button`
  font-family: 'BMHANNAAir';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 15px;
  font-size: 17px;
  width: 100%;
  height: 55px;
  cursor: pointer;
`;
