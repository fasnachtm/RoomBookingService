# Room Booking Service

## Prerequisites
- Java 17 or higher
- Maven 3.6.3 or higher
- Docker (optional, for running the application in a container)

## Building the Project
To build the project, navigate to the root directory of the project and run:

```mvn clean package```

This command will compile the code, run tests, and package the application into a JAR file.

## Running the Application
To run the application, you can use the following command:

```docker-compose up --build```

This will start the application on 
```localhost:8080```
 along with its dependencies defined in the `docker-compose.yml` file.

A swagger API documentation will be available at:
```http://localhost:8080/swagger-ui.html```

## API Endpoints

### Room Management

#### Create a Room
- **Endpoint:** `POST /api/rooms`
- **Content-Type:** `application/json`
- **Request Body:** 
```
json { "name": "Meeting Room 1", "location": "Floor 1" }
```

#### Get All Rooms
- **Endpoint:** `GET /api/rooms`
- **Success Response:** `200 OK`
    - Returns array of rooms

#### Get Room by ID
- **Endpoint:** `GET /api/rooms/{id}`
- **Success Response:** `200 OK`
    - Returns single room object
- **Error Response:** `404 Not Found` if room doesn't exist

#### Delete Room
- **Endpoint:** `DELETE /api/rooms/{id}`

 
### Booking Management

#### Create a Booking
- **Endpoint:** `POST /api/bookings`
- **Content-Type:** `application/json`
- **Request Body:**
```
json { "roomId": 1, "userName": "John Doe", "start": "2025-07-27T09:00:00", "end": "2025-07-27T10:00:00" }
``` 
- **Success Response:** `200 OK`
  - Body: `"Booking created with ID: {id}"`
- **Error Responses:**
  - `500 Internal Server Error` if booking overlaps or time range is invalid

#### Get Bookings
- **Endpoint:** `GET /api/bookings`
- **Query Parameters:**
  - `roomId` (optional): Filter by room
  - `startTime` (optional): Filter by start time
  - `endTime` (optional): Filter by end time
- **Success Response:** `200 OK`


#### Delete Booking
- **Endpoint:** `DELETE /api/bookings/{id}`
- **Success Response:** `200 OK`
  - Body: `"Booking deleted successfully."`
- **Error Responses:**
  - `404 Not Found` if booking doesn't exist
  - `500 Internal Server Error` for server errors

### Date Format Support
The API supports ISO date-time formats, e.g.:
- ISO Local Date Time: `"2025-07-27T09:00:00"`
```
