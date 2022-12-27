import { PropsWithChildrenC } from 'sokdak-util-types';

import React, { ReactNode } from 'react';

import { AxiosError } from 'axios';

interface ErrorBoundaryState {
  hasError: boolean;
  error: Error | AxiosError | null;
}

interface ErrorBoundaryProps {
  fallback: (error: Error | AxiosError | null, errorBoundaryReset: () => void) => ReactNode;
}

const initialState = { hasError: false, error: null };

class ErrorBoundary extends React.Component<PropsWithChildrenC<ErrorBoundaryProps>, ErrorBoundaryState> {
  constructor(props: PropsWithChildrenC<ErrorBoundaryProps>) {
    super(props);
    this.state = initialState;
  }

  static getDerivedStateFromError(error: Error) {
    return { hasError: true, error: error };
  }

  reset = () => {
    this.setState(initialState);
  };

  render() {
    if (this.state.hasError) {
      return this.props.fallback(this.state.error, this.reset);
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
