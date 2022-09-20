declare module '*.svg' {
  const ReactComponent: React.FunctionComponent<React.SVGProps<SVGSVGElement> & { title?: string }>;

  export default ReactComponent;
}
declare module '*.png';

declare module '*.ttf';
declare module '*.woff';
declare module '*.woff2';

// TODO: default props
