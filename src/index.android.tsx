import React, { useCallback, useEffect } from 'react';
import { View, StyleSheet, NativeEventEmitter, requireNativeComponent } from 'react-native';

export type BottomSheetState = 'hidden' | 'collapsed' | 'expanded';

interface PullUpProps {
  onSheetStateChanged?: (newState: BottomSheetState) => void;
  hideable?: boolean;
  collapsible?: boolean;
  fitToContents?: boolean;
  halfExpandedRatio?: number;
  expandedOffset?: number;
  peekHeight?: number;
  sheetState?: BottomSheetState;
  style?: object;
  children?: object;
}

const styles = StyleSheet.create({
  primary: { flex: 1 },
  sheet: { backgroundColor: 'white' },
});

const PullUpsView = requireNativeComponent('RNPullUpView');
const ContentView = requireNativeComponent('RNPullUpContentView');

const PullUps = (props: PullUpProps) => {
  const { children, renderContent, contentStyle, onSheetStateChanged, ...rest } = props;

  useEffect(() => {
    const eventEmitter = new NativeEventEmitter();
    const subscription = eventEmitter.addListener(
      'BottomSheetStateChange',
      (event) => {
        onSheetStateChanged?.(event);
      }
    );
    return () => subscription.remove();
  }, [onSheetStateChanged]);

  return (
    <PullUpsView {...rest} style={[ styles.primary, props.style ]}>
      { children }
      <ContentView style={[ styles.sheet, props.contentStyle ]}>{ renderContent() }</ContentView>
    </PullUpsView>
  );
};

export default PullUps;
