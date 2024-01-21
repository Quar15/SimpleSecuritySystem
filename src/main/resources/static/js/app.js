function updateColumnEventType(columnN) {
    console.log("Updated Columns");
    const eventColumnElements = document.querySelectorAll("main table td:nth-child(" + columnN + ")");
    
    eventColumnElements.forEach(e => {
        switch (e.textContent) {
            case "LOCKED":
            case "LOCK":
                e.innerHTML = '<i class="fa-solid fa-lock"></i>';
                break;
            case "UNLOCKED":
            case "UNLOCK":
                e.innerHTML = '<i class="fa-solid fa-lock-open"></i>';
                break;
            case "MOVEMENT_DETECTED":
                e.innerHTML = '<i class="fa-solid fa-location-crosshairs"></i>';
                break;
            default:
                e.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i>'
                break;
        }
    });
}
