package com.example.assettracker.service;

import com.example.assettracker.dto.AssetResponse;
import com.example.assettracker.dto.CreateAssetRequest;
import com.example.assettracker.exception.ResourceNotFoundException;
import com.example.assettracker.model.Asset;
import com.example.assettracker.repository.AssetRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * AssetService
 * ------------
 * Services contain business logic. They are simple POJOs annotated with
 * @Service so Spring will detect and manage them (as beans) during startup.
 *
 * This example uses an in-memory list to keep the example simple for students.
 * In production you would typically talk to a database via a Repository.
 */
@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;

    }

    // Return all assets. Demos simple mapping from model -> response DTO.
    public List<AssetResponse> getAssets(String status, String category, String location) {
        List<Asset> assets;

        if (hasValue(status)) {
            assets = assetRepository.findByStatusIgnoreCase(status.trim());
        } else if (hasValue(category)) {
            assets = assetRepository.findByCategoryIgnoreCase(category.trim());
        } else if (hasValue(location)) {
            assets = assetRepository.findByLocationContainsIgnoreCase(location.trim());
        } else {
            assets = assetRepository.findAll();
        }
        return assets.stream()
                .map(this::toResponse)
                .toList();
    }

    public Page<AssetResponse> getAssetsPaged(int page, int size, String sortBy, String direction) {
        // Implement pagination logic here using the repository
        // For example, you can use Spring Data's Pageable and PageRequest
        // to fetch a page of assets from the database.
        // This is a placeholder implementation.

        Sort sort = direction.equalsIgnoreCase("desc") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return assetRepository.findAll(pageable)
                .map(this::toResponse);
    }

    // Find an asset by id or throw a ResourceNotFoundException which is
    // handled globally by GlobalExceptionHandler.
    public AssetResponse getAssetById(String id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset " + id + " was not found"));

        return toResponse(asset);
    }

    // Create a new asset from the request DTO. Demonstrates simple mapping
    // from request -> response DTO and updating the in-memory store.
    public AssetResponse createAsset(CreateAssetRequest request) {
        Asset asset = new Asset(
                request.getAssetTag().trim(),
                request.getName().trim(),
                request.getCategory().trim(),
                request.getSerialNumber().trim(),
                "AVAILABLE",
                request.getLocation().trim(),
                null
        );

        Asset savedAsset = assetRepository.save(asset);
        return toResponse(savedAsset);
    }

    private boolean hasValue(String value) {
        return value != null && !value.trim().isBlank();
    }


    // Helper to convert an Asset to an AssetResponse DTO.
    private AssetResponse toResponse(Asset asset) {
        return new AssetResponse(
                asset.getId(),
                asset.getAssetTag(),
                asset.getName(),
                asset.getCategory(),
                asset.getSerialNumber(),
                asset.getStatus(),
                asset.getLocation(),
                asset.getAssignedTo()
        );
    }
}
