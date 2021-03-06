import { Container } from './index.styles';

import Spinner from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Spinner',
  component: Spinner,
} as ComponentMeta<typeof Spinner>;

const Template = () => (
  <Container>
    <Spinner />
  </Container>
);

export const SpinnerTemplate: ComponentStory<typeof Spinner> = Template.bind({});
