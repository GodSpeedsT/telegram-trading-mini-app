import type { AssetInfo, AssetWithMeta } from '../../../../shared/types/market';
import { historicalDatasets } from '../../../../shared/config/market-config';

export function getRandomAsset(): AssetInfo {
  const categories = Object.keys(historicalDatasets) as Array<keyof typeof historicalDatasets>;
  const randomCategory = categories[Math.floor(Math.random() * categories.length)];
  const assets = Object.keys(historicalDatasets[randomCategory]);
  const randomAsset = assets[Math.floor(Math.random() * assets.length)];

  return {
    category: randomCategory,
    assetKey: randomAsset,
    asset: historicalDatasets[randomCategory][randomAsset]
  };
}

export function getAsset(category: keyof typeof historicalDatasets, assetKey: string) {
  return historicalDatasets[category]?.[assetKey] || null;
}

export function getAllAssets(): AssetWithMeta[] {
  const allAssets: AssetWithMeta[] = [];

  (Object.keys(historicalDatasets) as Array<keyof typeof historicalDatasets>).forEach(category => {
    Object.keys(historicalDatasets[category]).forEach(assetKey => {
      allAssets.push({
        category,
        assetKey,
        ...historicalDatasets[category][assetKey]
      });
    });
  });

  return allAssets;
}

export function getChartData(category: keyof typeof historicalDatasets, assetKey: string, candleCount = 100) {
  const asset = getAsset(category, assetKey);
  if (!asset) return null;

  return {
    ...asset,
    data: asset.data.slice(0, candleCount)
  };
}
