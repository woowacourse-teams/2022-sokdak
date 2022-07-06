import Spinner from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';
import { Container } from './index.styles';

export default {
  title: 'Components/Spinner',
  component: Spinner,
} as ComponentMeta<typeof Spinner>;

const Template = (args: any) => (
  <Container>
    <Spinner {...args} />
  </Container>
);

export const SpinnerTemplate: ComponentStory<typeof Spinner> = Template.bind({});
SpinnerTemplate.args = {};
