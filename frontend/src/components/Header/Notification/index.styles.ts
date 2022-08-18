import { Link } from 'react-router-dom';

import NotificationImg from '@/assets/images/notification-icon.svg';

import styled from '@emotion/styled';

export const NotificationIcon = styled(NotificationImg)`
  width: 30px;
  height: 30px;
  position: relative;
`;

export const NotificationContainer = styled(Link)`
  width: fit-content;
  position: relative;
  transform: translateY(5%);
`;

export const AlarmPointer = styled.div`
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: ${props => props.theme.colors.sub};
  position: absolute;
  top: 15%;
  right: 25%;
  z-index: 2;
`;
